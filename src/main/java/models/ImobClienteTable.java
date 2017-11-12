/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 *
 * @author suporte
 */
public class ImobClienteTable {   
   
    private final SimpleIntegerProperty ImobClienteTableDataId;    
    private final SimpleStringProperty ImobClienteTableDataNome;
    private final SimpleStringProperty ImobClienteTableDataCnpj;
    private final SimpleStringProperty ImobClienteTableDataImob;    
    private final SimpleStringProperty ImobClienteTableDataStatus;


    public ImobClienteTable(int imobClienteTableDataId, String imobClienteTableDataStatus, String imobClienteTableDataNome, String imobClienteTableDataCnpj, String imobClienteTableDataImob) {
        ImobClienteTableDataId = new SimpleIntegerProperty(imobClienteTableDataId);
        ImobClienteTableDataStatus = new SimpleStringProperty(imobClienteTableDataStatus);
        ImobClienteTableDataNome = new SimpleStringProperty(imobClienteTableDataNome);
        ImobClienteTableDataCnpj = new SimpleStringProperty(imobClienteTableDataCnpj);
        ImobClienteTableDataImob = new SimpleStringProperty(imobClienteTableDataImob);
    }
    
    public int getImobClienteTableDataId() {
        return ImobClienteTableDataId.get();
    }

    public SimpleIntegerProperty imobClienteTableDataIdProperty() {
        return ImobClienteTableDataId;
    }

    public void setImobClienteTableDataId(int imobClienteTableDataId) {
        this.ImobClienteTableDataId.set(imobClienteTableDataId);
    }
    
    public String getImobClienteTableDataStatus() {
        return ImobClienteTableDataStatus.get();
    }

    public SimpleStringProperty imobClienteTableDataStatusProperty() {
        return ImobClienteTableDataStatus;
    }

    public void setImobClienteTableDataStatus(String imobClienteTableDataStatus) {
        this.ImobClienteTableDataStatus.set(imobClienteTableDataStatus);
    }
    
    public String getImobClienteTableDataNome() {
        return ImobClienteTableDataNome.get();
    }

    public SimpleStringProperty imobClienteTableDataNomeProperty() {
        return ImobClienteTableDataNome;
    }

    public void setImobClienteTableDataNome(String imobClienteTableDataNome) {
        this.ImobClienteTableDataNome.set(imobClienteTableDataNome);
    }
    
    public String getImobClienteTableDataCnpj() {
        return ImobClienteTableDataCnpj.get();
    }

    public SimpleStringProperty imobClienteTableDataCnpjProperty() {
        return ImobClienteTableDataCnpj;
    }

    public void setImobClienteTableDataCnpj(String imobClienteTableDataCnpj) {
        this.ImobClienteTableDataCnpj.set(imobClienteTableDataCnpj);
    }
    
    public String getImobClienteTableDataImob() {
        return ImobClienteTableDataImob.get();
    }

    public SimpleStringProperty imobClienteTableDataImobProperty() {
        return ImobClienteTableDataImob;
    }

    public void setImobClienteTableDataImob(String imobClienteTableDataImob) {
        this.ImobClienteTableDataImob.set(imobClienteTableDataImob);
    }


  
}
