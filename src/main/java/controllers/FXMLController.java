package controllers;

import models.ImobClienteTable;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lib.ConectDB;

public class FXMLController implements Initializable {    
    
    private ResultSet resultSet;
    private String temp;
    private boolean isImobClienteEditButtonClick;
    private boolean isImobClienteAddNewButtonClick;
    
    @FXML
    TableView<ImobClienteTable> imobClienteTableView; 
    @FXML
    TableColumn<ImobClienteTable,Integer> imobClienteColumnId;
    @FXML
    TableColumn<ImobClienteTable,String> imobClienteColumnNome;
    @FXML
    TableColumn<ImobClienteTable,String> imobClienteColumnCnpj;
    @FXML
    TableColumn<ImobClienteTable,String> imobClienteColumnImob;
    @FXML
    TableColumn<ImobClienteTable,String> imobClienteColumnStatus;
    
    
    @FXML
    private TextField imobClienteTFnome;
    @FXML
    private TextField imobClienteTFcnpj;
    @FXML
    private TextField imobClienteTFimob;    
    @FXML
    private CheckBox imobClienteCBstatus;
    
    @FXML
    private Button imobClienteLimparButtonClick;
    @FXML
    private Button imobClienteSalvarButtonClick;
    
    
    
   private ObservableList getDataFromClientesAndAddToObservableList(String query){
        ObservableList<ImobClienteTable> imobClienteTableData = FXCollections.observableArrayList();       
        ConectDB db = new ConectDB("localhost", System.getProperty("user.dir")+"/BASE.fdb", "SYSDBA", "masterkey");
        db.connect();
        this.resultSet = db.executar(query);
        String status = "";
        try {
            while(resultSet.next()){
                if(resultSet.getInt("STATUS") == 1){
                    status = "Liberado";
                } else {
                    status = "bloqueado";
                }
                imobClienteTableData.add(new ImobClienteTable(
                        resultSet.getInt("ID"),
                        status,
                        resultSet.getString("NOME"),
                        resultSet.getString("CNPJ"),
                        resultSet.getString("IMOB")
                ));
            }
            resultSet.close();
            db.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return imobClienteTableData;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imobClienteColumnId.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,Integer>("ImobClienteTableDataId"));
        imobClienteColumnNome.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,String>("ImobClienteTableDataNome"));
        imobClienteColumnCnpj.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,String>("ImobClienteTableDataCnpj"));
        imobClienteColumnImob.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,String>("ImobClienteTableDataImob"));
        imobClienteColumnStatus.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,String>("ImobClienteTableDataStatus"));
        
       imobClienteTableView.setItems(getDataFromClientesAndAddToObservableList("select *from Clientes"));
    }

    @FXML
    private void setImobClienterAddNewButtonClick(Event event){
        imobClienteSetAllEnable();
        isImobClienteAddNewButtonClick = true;
    }
    
     private void imobClienteSetAllEnable(){
        imobClienteTFnome.setDisable(false);
        imobClienteTFcnpj.setDisable(false);
        imobClienteTFimob.setDisable(false);       
        imobClienteCBstatus.setDisable(false);
        
        imobClienteLimparButtonClick.setDisable(false);
        imobClienteSalvarButtonClick.setDisable(false);
    }

    private void imobClienteSetAllDisable(){
        imobClienteTFnome.setDisable(true);
        imobClienteTFcnpj.setDisable(true);
        imobClienteTFimob.setDisable(true);
        imobClienteCBstatus.setDisable(true);       

       imobClienteLimparButtonClick.setDisable(true);
       imobClienteSalvarButtonClick.setDisable(true);
    }

    private void imobClienteSetAllLimpar(){
        imobClienteTFnome.clear();
        imobClienteTFcnpj.clear();
        imobClienteTFimob.clear();
        imobClienteCBstatus.setSelected(false);
    }
    
    
     @FXML
    private void setAdminTeacherEditButtonClick(Event event){
        ImobClienteTable getSelectedRow = imobClienteTableView.getSelectionModel().getSelectedItem();
        
        ConectDB db = new ConectDB("localhost", System.getProperty("user.dir")+"/BASE.fdb", "SYSDBA", "masterkey");
        
        String sqlQuery = "select * FROM clientes where clientes.id = '"+getSelectedRow.getImobClienteTableDataId()+"';";

        try {
           // connection = database.getConnection();
            db.connect();
            resultSet = db.executar(sqlQuery);
            imobClienteSetAllEnable();
            while(resultSet.next()) {
                imobClienteColumnNome.setText(resultSet.getString("NOME"));
                imobClienteColumnCnpj.setText(resultSet.getString("CNPJ"));
                imobClienteColumnImob.setText(resultSet.getString("IMOB"));
                if(resultSet.getInt("STATUS") == 1){
                    imobClienteCBstatus.isSelected();
                }
                
                

            }

            temp = String.valueOf(getSelectedRow.getImobClienteTableDataId());
            isImobClienteEditButtonClick = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    
}
