package database;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Suporte
 */
public class DataBaseFirebird implements Database {
    
    public Connection c;
    
  
    @Override
    public Connection conectar() {
        String url;
        String portNumber = "3050";        
        url = "jdbc:firebirdsql:" + "localhost" + "/" + portNumber + ":" + "F:/Base de dados/BASE.FDB";     
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
            this.c = DriverManager.getConnection(url, "SYSDBA", "masterkey");            
            return this.c;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseFirebird.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (InstantiationException ex) {
            Logger.getLogger(DataBaseFirebird.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DataBaseFirebird.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseFirebird.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }      
    }

    @Override
    public void desconectar(Connection connection) {

        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseFirebird.class.getName()).log(Level.SEVERE, null, ex);
        }

    }  
    
    public ResultSet executar(String query) {
        Statement st;
        ResultSet rs;
       
        try {
            st = this.c.createStatement();
            rs = st.executeQuery(query);
            return rs;
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
       
        return null;
    }
     
    public void update(String query) {
        Statement st;
        int rs;
       
        try {
            st = this.c.createStatement();
            rs = st.executeUpdate(query);
           
        } catch ( SQLException e ) {
            e.printStackTrace();
        }         
    }    
}
