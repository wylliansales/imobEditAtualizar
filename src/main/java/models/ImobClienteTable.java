/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.InputMismatchException;
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

    /**
     *
     */
    

    public ImobClienteTable(int imobClienteTableDataId, String imobClienteTableDataNome, String imobClienteTableDataCnpj, String imobClienteTableDataImob, String imobClienteTableDataStatus) {
        ImobClienteTableDataId = new SimpleIntegerProperty(imobClienteTableDataId);
        ImobClienteTableDataNome = new SimpleStringProperty(imobClienteTableDataNome);
        ImobClienteTableDataCnpj = new SimpleStringProperty(imobClienteTableDataCnpj);
        ImobClienteTableDataImob = new SimpleStringProperty(imobClienteTableDataImob);
        ImobClienteTableDataStatus = new SimpleStringProperty(imobClienteTableDataStatus);
    }
  
    
    //VALIDAÇÃO CONTROL C CONTROL V
    public static boolean isCNPJ(String CNPJ) {
// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
    if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
        CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
        CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
        CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
        CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
       (CNPJ.length() != 14))
       return(false);

    char dig13, dig14;
    int sm, i, r, num, peso;

// "try" - protege o código para eventuais erros de conversao de tipo (int)
    try {
// Calculo do 1o. Digito Verificador
      sm = 0;
      peso = 2;
      for (i=11; i>=0; i--) {
// converte o i-ésimo caractere do CNPJ em um número:
// por exemplo, transforma o caractere '0' no inteiro 0
// (48 eh a posição de '0' na tabela ASCII)
        num = (int)(CNPJ.charAt(i) - 48);
        sm = sm + (num * peso);
        peso = peso + 1;
        if (peso == 10)
           peso = 2;
      }

      r = sm % 11;
      if ((r == 0) || (r == 1))
         dig13 = '0';
      else dig13 = (char)((11-r) + 48);

// Calculo do 2o. Digito Verificador
      sm = 0;
      peso = 2;
      for (i=12; i>=0; i--) {
        num = (int)(CNPJ.charAt(i)- 48);
        sm = sm + (num * peso);
        peso = peso + 1;
        if (peso == 10)
           peso = 2;
      }

      r = sm % 11;
      if ((r == 0) || (r == 1))
         dig14 = '0';
      else dig14 = (char)((11-r) + 48);

// Verifica se os dígitos calculados conferem com os dígitos informados.
      if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
         return(true);
      else return(false);
    } catch (InputMismatchException erro) {
        return(false);
    }
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
