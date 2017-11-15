/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlmodels;

import controllers.FXMLController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.Criptografia;
import models.ImobClienteTable;

/**
 *
 * @author suporte
 */
public class ImobClienteSql {
      private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(ImobClienteTable imobClienteTable) {
        String sql = "INSERT INTO clientes(NOME, CNPJ, IMOB, STATUS) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, imobClienteTable.getImobClienteTableDataNome());
            stmt.setString(2, imobClienteTable.getImobClienteTableDataCnpj());
            stmt.setString(3, imobClienteTable.getImobClienteTableDataImob());
            stmt.setString(4, imobClienteTable.getImobClienteTableDataStatus());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ImobClienteSql.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
        public boolean alterar(ImobClienteTable imobClienteTable) {
        String sql = "UPDATE clientes SET nome=?, cnpj=?, imob=?, status=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, imobClienteTable.getImobClienteTableDataNome());
            stmt.setString(2, imobClienteTable.getImobClienteTableDataCnpj());
            stmt.setString(3, imobClienteTable.getImobClienteTableDataImob());
            stmt.setString(4, imobClienteTable.getImobClienteTableDataStatus());
            stmt.setInt(5, imobClienteTable.getImobClienteTableDataId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ImobClienteSql.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ImobClienteSql.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<ImobClienteTable> listar() {
        String sql = "SELECT * FROM clientes";
        List<ImobClienteTable> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            String status = null;
            while (resultado.next()) {                
                if("1".equalsIgnoreCase(resultado.getString("STATUS"))){
                    status = "LIBERADO";
                } else {
                    status = "BLOQUEADO";
                }
                ImobClienteTable imobClienteTable = new ImobClienteTable(resultado.getInt("ID"),resultado.getString("NOME")
                        ,resultado.getString("CNPJ"),resultado.getString("IMOB"),status);
                
                retorno.add(imobClienteTable);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImobClienteSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<ImobClienteTable> buscar(String nome) {
        String sql = "SELECT * FROM clientes WHERE nome CONTAINING ?"; 
        List<ImobClienteTable> retorno = new ArrayList<>();        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet resultado = stmt.executeQuery();
            String status = null;
            while (resultado.next()) {                
                if("1".equalsIgnoreCase(resultado.getString("STATUS"))){
                    status = "LIBERADO";
                } else {
                    status = "BLOQUEADO";
                }         
                
                retorno.add(new ImobClienteTable(resultado.getInt("ID"), resultado.getString("NOME")
                        ,resultado.getString("CNPJ"),resultado.getString("IMOB"),status));               
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImobClienteSql.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return retorno;
    }
    
    
    public ImobClienteTable buscarUm(int id) {
        String sql = "SELECT * FROM clientes WHERE id=?"; 
        ImobClienteTable retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            String status = null;
            if (resultado.next()) {
                if("1".equalsIgnoreCase(resultado.getString("STATUS"))){
                    status = "LIBERADO";
                } else {
                    status = "BLQOUEADO";
                }
                retorno = new ImobClienteTable(resultado.getInt("ID"), resultado.getString("NOME")
                        ,resultado.getString("CNPJ"),resultado.getString("IMOB"),status); 
                retorno.setImobClienteTableDataId(resultado.getInt("ID"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImobClienteSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    
     public String clienteCnpjStatus() throws Exception {
        String sql = "SELECT CNPJ, STATUS FROM clientes";
        String conteudo = "";
       
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet  resultado = stmt.executeQuery(); 
            try {
            while(resultado.next()){
                conteudo += Criptografia.criptografar(resultado.getString("CNPJ") + "=" + resultado.getString("STATUS")) + "\r\n";
            }} catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch ( SQLException e ) {
            e.printStackTrace();
            return null;
        }   
       
        return conteudo;
    }
}
