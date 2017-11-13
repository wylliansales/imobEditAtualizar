package controllers;

import java.io.File;
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
import javax.swing.JOptionPane;
import lib.Arquivo;
import database.DataBaseFirebird;
import database.Database;
import java.sql.Connection;
import java.util.List;
import lib.Criptografia;
import lib.DropBox;
import lib.Msg;
import sqlmodels.ImobClienteSql;

public class FXMLController implements Initializable {    
        
    private int temp;
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
    
    //private List<ImobClienteTable> listaImobClientes;    
   // private ObservableList<ImobClienteTable> imobClienteTableData;
    private final Database database = new DataBaseFirebird();
    private final Connection connection = database.conectar();
    private final ImobClienteSql  imobClientDB = new ImobClienteSql();
    
   private ObservableList getDataFromClientesAndAddToObservableList(){        
        return FXCollections.observableArrayList(imobClientDB.listar());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imobClientDB.setConnection(connection);
        imobClienteColumnId.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,Integer>("ImobClienteTableDataId"));
        imobClienteColumnNome.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,String>("ImobClienteTableDataNome"));
        imobClienteColumnCnpj.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,String>("ImobClienteTableDataCnpj"));
        imobClienteColumnImob.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,String>("ImobClienteTableDataImob"));
        imobClienteColumnStatus.setCellValueFactory(new PropertyValueFactory<ImobClienteTable,String>("ImobClienteTableDataStatus"));
        
       imobClienteTableView.setItems(getDataFromClientesAndAddToObservableList());
    }

    @FXML
    private void setImobClienterAddNewButtonClick(Event event){
        imobClienteSetAllEnable();
        isImobClienteAddNewButtonClick = true;
        isImobClienteEditButtonClick = false;
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

        ImobClienteTable imobCliente = imobClientDB.buscarUm(getSelectedRow.getImobClienteTableDataId());

        imobClienteSetAllEnable();

        imobClienteTFnome.setText(imobCliente.getImobClienteTableDataNome());
        imobClienteTFcnpj.setText(imobCliente.getImobClienteTableDataCnpj());
        imobClienteTFimob.setText(imobCliente.getImobClienteTableDataImob());
        if ("0".equalsIgnoreCase(imobCliente.getImobClienteTableDataStatus())) {
            imobClienteCBstatus.setSelected(true);
        }
        temp = imobCliente.getImobClienteTableDataId();
        isImobClienteEditButtonClick = true;
        isImobClienteAddNewButtonClick = false;
    }        
           


    
    @FXML
    private void setImobClienteSalvarButtonClick(Event event){        
         String status="1";
         if(imobClienteCBstatus.isSelected()){
             status = "0";
         }    
       
            if(isImobClienteAddNewButtonClick){   
                ImobClienteTable imobCliente = new ImobClienteTable(0,imobClienteTFnome.getText(), imobClienteTFcnpj.getText(), imobClienteTFimob.getText(),status);    
                if(!imobClientDB.inserir(imobCliente)){
                    Msg.msg("error", "Não foi possível cadastrar o cliente", "Cadastro de cliente");
                }
            }
            else if (isImobClienteEditButtonClick){ 
                ImobClienteTable imobCliente = new ImobClienteTable(temp,imobClienteTFnome.getText(), imobClienteTFcnpj.getText(), imobClienteTFimob.getText(),status);
                if(!imobClientDB.alterar(imobCliente)){
                    Msg.msg("error", "Não foi possível atualizar o cliente", "Atualizar cliente");
                }
            }        
       
        imobClienteSetAllLimpar();
        imobClienteSetAllDisable();
        imobClienteTableView.setItems(getDataFromClientesAndAddToObservableList());
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
        ImobClienteTable getSelectedRow = imobClienteTableView.getSelectionModel().getSelectedItem();
                           
        if(!imobClientDB.remover(getSelectedRow.getImobClienteTableDataId())){
            Msg.msg("error", "Não foi possível excluir o cliente", "Excluindo cliente");
        }       
        imobClienteTableView.setItems(getDataFromClientesAndAddToObservableList());
           
        
       
    }
    
    @FXML
    private void setUpClientesButtonClick(Event event) throws Exception{
     /*  DataBaseFirebird db = new DataBaseFirebird(this.host, this.banco, this.user, this.senha);
       db.connect();
       String query = "SELECT CNPJ, STATUS FROM CLIENTES";
       resultSet = db.executar(query);
       String conteudo = "";
        try {
            while(resultSet.next()){
                conteudo += Criptografia.criptografar(resultSet.getString("CNPJ") + "=" + resultSet.getString("STATUS")) + "\r\n";
            }} catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
      //  resultSet.close();
        Arquivo.criarFile(conteudo, "0000000000.txt");        
        DropBox box = new DropBox();
        box.delete("/0000000000.txt");
        box.upload("0000000000.txt");
        File f = new File("0000000000.txt");
        f.delete();

        
        
       JOptionPane.showMessageDialog(null, "Em desenvolvimento!");*/
    }
    
    @FXML
    private void setUpImobButtonClick(Event event){
        JOptionPane.showMessageDialog(null, "Em desenvolvimento!");
    }

    @FXML
    private void setimobClienteBuscarButtonClick(Event event){
        String sqlQuery = "select * FROM clientes where nome CONTAINING '"+imobClienteTFbuscar.getText()+"';";        
        List<ImobClienteTable> lista = imobClientDB.buscar(imobClienteTFbuscar.getText());
        
        if(lista.isEmpty()){
           imobClienteTableView.setItems(getDataFromClientesAndAddToObservableList()); 
        }else {
           imobClienteTableView.setItems(FXCollections.observableArrayList(lista));  
        }      
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
    
    @FXML
    private void setImobClienteConfigButtonClick(Event event){
        JOptionPane.showMessageDialog(null, "Em desenvolvimento!");
    }
    
    
    
}
