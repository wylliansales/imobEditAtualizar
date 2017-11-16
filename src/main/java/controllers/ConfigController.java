/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.DataBaseFirebird;
import database.Database;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lib.Criptografia;
import lib.Msg;
import models.ConfigTable;
import sqlmodels.ConfigSql;
import sqlmodels.ImobClienteSql;

/**
 * FXML Controller class
 *
 * @author suporte
 */
public class ConfigController implements Initializable {

    
    @FXML
    private TextField configTFsenha;
    @FXML
    private TextField configTFemail;
    @FXML
    private TextField configTFtoken;

    
    private final Database database = new DataBaseFirebird();
    private final Connection connection = database.conectar();
    private final ConfigSql  configDB = new ConfigSql();
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configDB.setConnection(connection);
        try {
            buscarDados();
        } catch (Exception ex) {
            Logger.getLogger(ConfigController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    
    private void buscarDados() throws Exception{
        ConfigTable config = configDB.buscarUm(1);
        
        configTFtoken.setText(config.getToken().equalsIgnoreCase("")?"":config.getToken());
        configTFemail.setText(config.getEmail() == null?"":config.getEmail());
        configTFsenha.setText(config.getSenha() == null?"":Criptografia.descriptografar(config.getSenha()));
        
    }
    
    @FXML
    private void setImobConfigSalvarButtonClick(Event event) throws Exception{
        ConfigTable config = new ConfigTable(
                configTFtoken.getText(), 
                configTFemail.getText(), 
                Criptografia.criptografar(configTFsenha.getText()));
        config.setId(1);
        configDB.alterar(config);
        Msg.msg("info", "Salvo com sucesso!", "Configuração");
        Stage stage = (Stage) configTFemail.getScene().getWindow();
        stage.close();
    }
    
}
