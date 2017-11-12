package lib;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Suporte
 */
public class Arquivo {

    public static String lerTxtVerif(String arquiv, String cnpj) throws FileNotFoundException, IOException, Exception {
        FileReader file = new FileReader(new File(arquiv));
        BufferedReader ler = new BufferedReader(file);
        String linha = null;

        while ((linha = ler.readLine()) != null) {
            linha =  Criptografia.descriptografar(linha);            
                return linha.substring(15);          
            
        }
        return "Cadastro não localizado";
    }
    
    

    
    
    
    
    
  /*  public static void atualizar() throws FileNotFoundException, IOException, DbxException{
        //LENDO ARQUIVO CONFIG      
       
        //COMPACTAR O ARQUIVO
        
        File file10 = new File("");

                if (file10.exists()) {
                    int cont;
                    byte[] dados = new byte[(int) file10.length()];
                    try {

                        FileOutputStream destino = new FileOutputStream("banco.zip");
                        ZipOutputStream saida = new ZipOutputStream(new BufferedOutputStream(destino));

                        FileInputStream streamEntrada = new FileInputStream(file10);
                        BufferedInputStream origem = new BufferedInputStream(streamEntrada, (int) file10.length());
                        ZipEntry entry = new ZipEntry(file10.getName());
                        saida.putNextEntry(entry);

                        while ((cont = origem.read(dados, 0, (int) arquivo.length())) != -1) {
                            saida.write(dados, 0, cont);
                        }

                        origem.close();
                        saida.close();
                    } catch (FileNotFoundException ex) {
                        System.out.println("Erro na compactação");
                    } catch (IOException ex) {
                        System.out.println("Erro ao compactar");
                    }
                    System.out.println("Arquivo compactado");
                }*/
                
        
        
        
                
        

        // Get files and folder metadata from Dropbox root directory
        /*ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }*/         
         // Upload "test.txt" to Dropbox 
        
        
        
        
        /////////////////////////////////////////////////////////////
        
    
}
