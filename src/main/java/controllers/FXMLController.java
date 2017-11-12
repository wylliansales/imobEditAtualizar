package controllers;

import java.io.IOException;
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
import lib.ConnectDB;

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
    private TextField imobClienteTFbuscar;
    
    
    
    @FXML
    private Button imobClienteLimparButtonClick;
    @FXML
    private Button imobClienteSalvarButtonClick;
    
    
    
   private ObservableList getDataFromClientesAndAddToObservableList(String query){
        ObservableList<ImobClienteTable> imobClienteTableData = FXCollections.observableArrayList();       
        ConnectDB db = new ConnectDB("localhost", System.getProperty("user.dir")+"/BASE.fdb", "SYSDBA", "masterkey");
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
    private void setImobClienteEditButtonClick(Event event){
        ImobClienteTable getSelectedRow = imobClienteTableView.getSelectionModel().getSelectedItem();
        
        ConnectDB db = new ConnectDB("localhost", System.getProperty("user.dir")+"/BASE.fdb", "SYSDBA", "masterkey");
        
        String sqlQuery = "select * FROM clientes where id = '"+getSelectedRow.getImobClienteTableDataId()+"';";
              
          
            db.connect();
            resultSet = db.executar(sqlQuery);
              imobClienteSetAllEnable();
        try {
            while(resultSet.next()) {
                imobClienteTFnome.setText(resultSet.getString("NOME"));
                imobClienteTFcnpj.setText(resultSet.getString("CNPJ"));
                imobClienteTFimob.setText(resultSet.getString("IMOB"));
                if(resultSet.getInt("STATUS") == 0){
                imobClienteCBstatus.setSelected(true);
                }                       

            }
            temp = String.valueOf(getSelectedRow.getImobClienteTableDataId());
            isImobClienteEditButtonClick = true;
            db.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }     

    }
    
    @FXML
    private void setImobClienteSalvarButtonClick(Event event){
         ConnectDB db = new ConnectDB("localhost", System.getProperty("user.dir")+"/BASE.fdb", "SYSDBA", "masterkey");
         String status="1";
         if(imobClienteCBstatus.isSelected()){
             status = "0";
         }
       
            db.connect();
            if(isImobClienteAddNewButtonClick){
                db.update("insert into `clientes` (`NOME`,`CNPJ`,`IMOB`,`STATUS`) values ('"+
                        imobClienteTFnome.getText()+"','"+imobClienteTFcnpj.getText()+"','"+imobClienteTFimob.getText()+"','"+status
                        +"') ;");
            }
            else if (isImobClienteEditButtonClick){               
                db.update("update clientes set "+
                        "NOME = '"+imobClienteTFnome.getText()+"',"+
                        "CNPJ = '"+imobClienteTFcnpj.getText()+"',"+
                        "IMOB = '"+imobClienteTFimob.getText()+"',"+
                        "STATUS = '"+status+"'"+
                        " where id = '"+temp+"';");
            }


            db.disconnect();           
       
        imobClienteSetAllLimpar();
        imobClienteSetAllDisable();
        imobClienteTableView.setItems(getDataFromClientesAndAddToObservableList("select *from Clientes"));
        isImobClienteAddNewButtonClick=false;
        isImobClienteEditButtonClick = false;
    }

    @FXML
    private void setImobClienteLimparButtonClick(Event event){
        imobClienteSetAllLimpar();
        imobClienteSetAllDisable();
        isImobClienteAddNewButtonClick = false;
        isImobClienteEditButtonClick = false;
    }
    
    @FXML
    private void setImobClienteDeleteButtonClick(Event event){
        ConnectDB db = new ConnectDB("localhost", System.getProperty("user.dir")+"/BASE.fdb", "SYSDBA", "masterkey");
        ImobClienteTable getSelectedRow = imobClienteTableView.getSelectionModel().getSelectedItem();
        String sqlQuery = "delete from clientes where id = '"+getSelectedRow.getImobClienteTableDataId()+"';";
          
           db.connect();
           db.update(sqlQuery);
            imobClienteTableView.setItems(getDataFromClientesAndAddToObservableList("SELECT * FROM clientes;"));
            db.disconnect();
        
       
    }
    
    @FXML
    private void setAtualizarClientesButtonClick(Event event){
       //adminTeacherTableView.setItems(getDataFromTeacherAndAddToObservableList("SELECT * FROM teacher;"));//sql Query
       // adminTeacherTFSearch.clear();
    }

    @FXML
    private void setimobClienteBuscarButtonClick(Event event){
        String sqlQuery = "select * FROM clientes where nome CONTAINING '"+imobClienteTFbuscar.getText()+"';";
        imobClienteTableView.setItems(getDataFromClientesAndAddToObservableList(sqlQuery));
        imobClienteTFbuscar.clear();
    }

    @FXML
    private void setCourseAboutButtonClick(Event event) throws IOException {
     //   menuBarControl.about();
    }

    @FXML
    private void setCourseCloseButtonClick(Event event){
       // menuBarControl.close();
    }
    
}
