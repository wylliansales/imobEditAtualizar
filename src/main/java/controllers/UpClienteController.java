/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.DataBaseFirebird;
import database.Database;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lib.Arquivo;
import lib.DropBox;
import lib.Msg;
import sqlmodels.ImobClienteSql;

/**
 * FXML Controller class
 *
 * @author suporte
 */
public class UpClienteController implements Initializable {
    private static final long CHUNKED_UPLOAD_CHUNK_SIZE = 1L << 20;
     
    @FXML
    private AnchorPane anchorPaneUpCliente;
    
    @FXML 
    private ProgressIndicator progressIndicator;
    
    private final Database database = new DataBaseFirebird();
    private final Connection connection = database.conectar();
    private final ImobClienteSql  imobClientDB = new ImobClienteSql();
    DropBox box;    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        imobClientDB.setConnection(connection);
        box = new DropBox();
        Task task = taskWorker(10);
        progressIndicator.progressProperty().unbind();
        progressIndicator.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
		@Override
		public void handle(final WorkerStateEvent event) {
		  Stage stage = (Stage) anchorPaneUpCliente.getScene().getWindow();
                  stage.close();
		}
         });
        Thread thread = new Thread(task);
        thread.start();
    }    
    
    private Task taskWorker(final int seconds) {
        return new Task() {
            @Override
            protected Object call() throws Exception {               
                String conteudo = imobClientDB.clienteCnpjStatus();                
                Arquivo.criarFile(conteudo, "0000000000.txt");                
                box.delete("0000000000.txt");  
              /*  File upFile = new File("0000000000.txt");
                if(upFile.length() <=(2 * CHUNKED_UPLOAD_CHUNK_SIZE)){
                        box.uploadFile(box.getClient(), upFile, "/0000000000.txt");
                    }else{
                        box.chunkedUploadFile(box.getClient(), upFile, "/0000000000.txt");
                    }             */     
                Arquivo.deleteFile("0000000000.txt");
                updateProgress(10, seconds);
                Thread.sleep(1000);
                /* for (int i = 6; i < seconds; i++) {
                   updateProgress(i + 1, seconds);
                   Thread.sleep(80);
               }*/
                return true;
            }
        ;
    };
   }
    
}
