package lib;




import database.DataBaseFirebird;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.SearchBuilder;
import com.dropbox.core.v2.users.FullAccount;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;


public class DropBox{    
    DbxClientV2 client;
    DbxRequestConfig config;        
    public DropBox(){
        // Create Dropbox client
         config = new DbxRequestConfig("dropbox/java-tutorial", Locale.getDefault().toString());
         client = new DbxClientV2(config, getToken());
    }    
     
     
     public void upload(String arquivo) throws DbxException, FileNotFoundException, IOException{ 
        
        try (InputStream in = new FileInputStream(arquivo)) {
            FileMetadata metadata = client.files().uploadBuilder("/"+arquivo)
                .uploadAndFinish(in);
            in.close();
        }
        
     }   
     
    public void dowloand(String location, String remote) throws FileNotFoundException, DbxException, IOException {
        File arquivo = new File(location);
        if (!arquivo.exists()) {
            arquivo.createNewFile();
        }
        FileOutputStream output = new FileOutputStream(location);
        client.files().downloadBuilder(remote).download(output);
    }

    public void delete(String locationRemote) throws FileNotFoundException, DbxException, IOException {        
        client.files().deleteV2(locationRemote);
    }
     
     public void getInfoClient() throws DbxException{
         // Get current account info
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());
     }
 
    public void listArquivos() throws DbxException {
        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }
            if (!result.getHasMore()) {
                break;
            }
            result = client.files().listFolderContinue(result.getCursor());
        }
    }
    
    private String getToken(){
        DataBaseFirebird db = new DataBaseFirebird();
        Connection connection;
        String query = "SELECT TOKEN FROM SETUP";
        connection = db.conectar();
        ResultSet resultSet = db.executar(query);
        String token = null;
        try {
            while(resultSet.next()){
                token = resultSet.getString("TOKEN");
            }
        db.desconectar(connection);
        resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(DropBox.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        if(token == null || token == ""){
            Msg.msg("error", "Procedimento não concluído, token não cadastrado", "Obtendo TOKEN");
            JOptionPane.showMessageDialog(null, token);
        } else {
            return token;
        }      
        return token;
    }
}
