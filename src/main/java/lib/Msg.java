/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import javafx.scene.control.Alert;

/**
 *
 * @author suporte
 */
public class Msg {
    
    public static void msg(String tipo, String msg, String title){
        if(tipo.equalsIgnoreCase("error")){
            Alert dialogoErro = new Alert(Alert.AlertType.ERROR);
            dialogoErro.setTitle("Diálogo");
            dialogoErro.setHeaderText(title);
            dialogoErro.setContentText(msg);
            dialogoErro.showAndWait();
        } else if(tipo.equalsIgnoreCase("info")){
            Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
            dialogoInfo.setTitle("Diálogo");
            dialogoInfo.setHeaderText(title);
            dialogoInfo.setContentText(msg);
            dialogoInfo.showAndWait();
        } else if(tipo.equalsIgnoreCase("warn")){
            Alert dialogoAviso = new Alert(Alert.AlertType.WARNING);
            dialogoAviso.setTitle("Diálogo");
            dialogoAviso.setHeaderText(title);
            dialogoAviso.setContentText(msg);
            dialogoAviso.showAndWait();
        }
            
    }   

}
