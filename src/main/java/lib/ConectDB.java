package lib;

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
public class ConectDB {
    private String host;
    private String user;
    private String pass;
    private String database;
    
    public Connection c;
    
    public ConectDB ( String host, String database, String user, String pass ) {
        this.pass = pass;
        this.user = user;
        this.host = host;
        this.database = database;
    }
    
    public boolean connect() {
        boolean isConnected = false;

        String url;
        String portNumber = "3050";
        String userName = this.user;
        String passName = this.pass;
        url = "jdbc:firebirdsql:" + this.host + "/" + portNumber + ":" + this.database;
        System.out.println(url);
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
            this.c = DriverManager.getConnection(url, userName, passName);            
            isConnected = true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            isConnected = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            isConnected = false;
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            isConnected = false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            isConnected = false;
        }

        return isConnected;
    }
    
    public boolean disconnect() {
        boolean isConnected = false;

        String url;
        String portNumber = "3050";
        String userName = this.user;
        String passName = this.pass;
        url = "jdbc:firebirdsql:" + this.host + "/" + portNumber + ":" + this.database;

        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
            this.c = DriverManager.getConnection(url, userName, passName);
            this.c.close();
            isConnected = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            isConnected = false;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            isConnected = false;
        } catch (InstantiationException e) {
            System.out.println(e.getMessage());
            isConnected = false;
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
            isConnected = false;
        }

        return isConnected;
    }

     public ResultSet executar( String query ) {
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
     
     
    public static String[] getEmpresaCnpj(ConectDB cc){
         String dados[]  = new String[2];
         
        if(cc.connect()){
            System.out.println("conectado");
        } else {
            System.out.println("Não conectado");
        }
        
        ResultSet rs = cc.executar("SELECT *FROM setup");

        try {
            while (rs.next()) {
                
                dados[0] = rs.getString("SET_CNPJ");
                if (!(dados[0] == null)) {
                    dados[0] = dados[0].replace(".", "");
                    dados[0] = dados[0].replace("/", "");
                    dados[0] = dados[0].replace("-", "");
                }                
                dados[1] = rs.getString("EMPRESA");
                
                if(dados[0] == null || dados[1] == null) {
                    JOptionPane.showMessageDialog(null, "Informaçõe necessária não cadastrada no sistema");
                System.exit(0);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Algum error aconteceu com o host, entre em contato com suporte");

            System.exit(0);
        }
        return dados;
    }


}
