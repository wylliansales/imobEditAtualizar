/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlmodels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.ConfigTable;
import models.ImobClienteTable;

/**
 *
 * @author suporte
 */
public class ConfigSql {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public ConfigTable buscarUm(int id) {
        String sql = "SELECT * FROM setup WHERE id=?"; 
        ConfigTable retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            String status = null;
            if (resultado.next()) {
              
                retorno = new ConfigTable(
                        resultado.getString("TOKEN"), 
                        resultado.getString("EMAIL"),
                        resultado.getString("SENHA")
                        );
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImobClienteSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    
    public boolean alterar(ConfigTable configTable) {
        String sql = "UPDATE setup SET token=?, email=?, senha=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, configTable.getToken());
            stmt.setString(2, configTable.getEmail());
            stmt.setString(3, configTable.getSenha());
            stmt.setInt(4, configTable.getId());            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ImobClienteSql.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
