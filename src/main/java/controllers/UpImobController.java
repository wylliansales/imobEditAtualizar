/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.dropbox.core.DbxException;
import com.dropbox.core.NetworkIOException;
import com.dropbox.core.RetryException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CommitInfo;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadSessionCursor;
import com.dropbox.core.v2.files.UploadSessionFinishErrorException;
import com.dropbox.core.v2.files.UploadSessionLookupErrorException;
import com.dropbox.core.v2.files.WriteMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.zip.ZipOutputStream;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import lib.Arquivo;
import lib.DropBox;


/**
 * FXML Controller class
 *
 * @author suporte
 */
public class UpImobController implements Initializable {
    
    
    @FXML
    private ProgressIndicator progressIndicator;
    private static final long CHUNKED_UPLOAD_CHUNK_SIZE = 1L << 20;
    private static final int CHUNKED_UPLOAD_MAX_ATTEMPTS = 5;
    @FXML
    private AnchorPane anchorPaneUpImob;
    

    DropBox box;   
    File arquivo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFileChooser seletor = new JFileChooser("C:\\");        
        seletor.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);       
        int i = seletor.showOpenDialog(null);        
                  
         if (i == 1) {
              Stage stage = (Stage) anchorPaneUpImob.getScene().getWindow();
              stage.close();
        } else {
             arquivo = seletor.getSelectedFile();
             
             box = new DropBox();
             Task task = taskWorker(10);
             progressIndicator.progressProperty().unbind();
             progressIndicator.progressProperty().bind(task.progressProperty());             
             task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
		@Override
		public void handle(final WorkerStateEvent event) {
		  Stage stage = (Stage) anchorPaneUpImob.getScene().getWindow();
                  stage.close();
		}
         });
        Thread thread = new Thread(task);
        thread.start();
         }       
    }

    private Task taskWorker(final int seconds) {
            return new Task() {
            @Override
            protected Object call() throws Exception {

                if (arquivo.isFile()) {
                    Arquivo.compactaFile(arquivo, "update.zip");
                } else {
                    Arquivo.compactFolder(arquivo.getPath(), "update.zip", ZipOutputStream.STORED);
                }

                box.delete("update.zip");
                File upFile = new File(System.getProperty("user.dir") + "/update.zip");
                if (upFile.length() <= (2 * CHUNKED_UPLOAD_CHUNK_SIZE)) {
                    box.uploadFile(box.getClient(), upFile, "/update.zip");
                    updateProgress(10, seconds);
                } else {
                    chunkedUploadFile(box.getClient(), upFile, "/update.zip");
                }
                Thread.sleep(1000);
                return true;
            }

            ;

                 public void chunkedUploadFile(DbxClientV2 dbxClient, File localFile, String dropboxPath) {
                long size = localFile.length();
                System.out.println(CHUNKED_UPLOAD_CHUNK_SIZE);
                // assert our file is at least the chunk upload size. We make this assumption in the code
                // below to simplify the logic.
                if (size < CHUNKED_UPLOAD_CHUNK_SIZE) {
                    System.err.println("File too small, use upload() instead.");
                    System.exit(1);
                    return;
                }

                long uploaded = 0L;
                DbxException thrown = null;

                // Chunked uploads have 3 phases, each of which can accept uploaded bytes:
                //
                //    (1)  Start: initiate the upload and get an upload session ID
                //    (2) Append: upload chunks of the file to append to our session
                //    (3) Finish: commit the upload and close the session
                //
                // We track how many bytes we uploaded to determine which phase we should be in.
                String sessionId = null;
                for (int i = 0; i < CHUNKED_UPLOAD_MAX_ATTEMPTS; ++i) {
                    if (i > 0) {
                        System.out.printf("Retrying chunked upload (%d / %d attempts)\n", i + 1, CHUNKED_UPLOAD_MAX_ATTEMPTS);
                    }

                    try (InputStream in = new FileInputStream(localFile)) {
                        // if this is a retry, make sure seek to the correct offset
                        in.skip(uploaded);

                        // (1) Start
                        if (sessionId == null) {
                            sessionId = dbxClient.files().uploadSessionStart()
                                    .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE)
                                    .getSessionId();
                            uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                            printProgress(uploaded, size);
                        }

                        UploadSessionCursor cursor = new UploadSessionCursor(sessionId, uploaded);

                        // (2) Append
                        while ((size - uploaded) > CHUNKED_UPLOAD_CHUNK_SIZE) {
                            dbxClient.files().uploadSessionAppendV2(cursor)
                                    .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE);
                            uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                            printProgress(uploaded, size);
                            cursor = new UploadSessionCursor(sessionId, uploaded);
                        }

                        // (3) Finish
                        long remaining = size - uploaded;
                        CommitInfo commitInfo = CommitInfo.newBuilder(dropboxPath)
                                .withMode(WriteMode.ADD)
                                .withClientModified(new Date(localFile.lastModified()))
                                .build();
                        FileMetadata metadata = dbxClient.files().uploadSessionFinish(cursor, commitInfo)
                                .uploadAndFinish(in, remaining);

                        System.out.println(metadata.toStringMultiline());
                        return;
                    } catch (RetryException ex) {
                        thrown = ex;
                        // RetryExceptions are never automatically retried by the client for uploads. Must
                        // catch this exception even if DbxRequestConfig.getMaxRetries() > 0.
                        sleepQuietly(ex.getBackoffMillis());
                        continue;
                    } catch (NetworkIOException ex) {
                        thrown = ex;
                        // network issue with Dropbox (maybe a timeout?) try again
                        continue;
                    } catch (UploadSessionLookupErrorException ex) {
                        if (ex.errorValue.isIncorrectOffset()) {
                            thrown = ex;
                            // server offset into the stream doesn't match our offset (uploaded). Seek to
                            // the expected offset according to the server and try again.
                            uploaded = ex.errorValue
                                    .getIncorrectOffsetValue()
                                    .getCorrectOffset();
                            continue;
                        } else {
                            // Some other error occurred, give up.
                            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                            System.exit(1);
                            return;
                        }
                    } catch (UploadSessionFinishErrorException ex) {
                        if (ex.errorValue.isLookupFailed() && ex.errorValue.getLookupFailedValue().isIncorrectOffset()) {
                            thrown = ex;
                            // server offset into the stream doesn't match our offset (uploaded). Seek to
                            // the expected offset according to the server and try again.
                            uploaded = ex.errorValue
                                    .getLookupFailedValue()
                                    .getIncorrectOffsetValue()
                                    .getCorrectOffset();
                            continue;
                        } else {
                            // some other error occurred, give up.
                            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                            System.exit(1);
                            return;
                        }
                    } catch (DbxException ex) {
                        System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                        System.exit(1);
                        return;
                    } catch (IOException ex) {
                        System.err.println("Error reading from file \"" + localFile + "\": " + ex.getMessage());
                        System.exit(1);
                        return;
                    }
                }

                // if we made it here, then we must have run out of attempts
                System.err.println("Maxed out upload attempts to Dropbox. Most recent error: " + thrown.getMessage());
                System.exit(1);
            }

            private void printProgress(long uploaded, long size) {
                System.out.printf("Uploaded %12d / %12d bytes (%5.2f%%)\n", uploaded, size, 100 * (uploaded / (double) size));
                updateProgress(uploaded,size);
            }

            private void sleepQuietly(long millis) {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException ex) {
                    // just exit
                    System.err.println("Error uploading to Dropbox: interrupted during backoff.");
                    System.exit(1);
                }
            }
        };

    }
}
