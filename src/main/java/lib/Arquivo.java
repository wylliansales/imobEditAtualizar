package lib;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;

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
    
    public static void criarFile(String conteudo,String nomeArquivo){
        File f = new File(nomeArquivo);
        
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                   
        try {
          FileWriter  fw = new FileWriter(f);
          BufferedWriter gravar = new BufferedWriter(fw);          
          gravar.write(conteudo);
          gravar.flush();
          gravar.close();
        } catch (IOException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
            Msg.msg("error", "Procedimento não concluído, ocorreu um erro....!", "Gravação de arquivo");
        }
                
    }

    public static void compactaFile(File arquivo, String outputFileName) {
        if (arquivo.exists()) {
            int cont;
            byte[] dados = new byte[(int) arquivo.length()];
            try {

                FileOutputStream destino = new FileOutputStream(outputFileName);
                ZipOutputStream saida = new ZipOutputStream(new BufferedOutputStream(destino));

                FileInputStream streamEntrada = new FileInputStream(arquivo);
                BufferedInputStream origem = new BufferedInputStream(streamEntrada, (int) arquivo.length());
                ZipEntry entry = new ZipEntry(arquivo.getName());
                saida.putNextEntry(entry);

                while ((cont = origem.read(dados, 0, (int) arquivo.length())) != -1) {
                    saida.write(dados, 0, cont);
                }

                origem.close();
                saida.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Erro ao compactar");
            } catch (IOException ex) {
                System.out.println("Erro ao compactar");
            }
        }
    } 
    
    
    
    
    public static void compactFolder(String sourceFolderName, String outputFileName, int level)
            throws FileNotFoundException, Exception, IOException {
        FileOutputStream fos = new FileOutputStream(outputFileName);
        ZipOutputStream zos = new ZipOutputStream(fos);
        // level - the compression level (0-9)
        zos.setLevel(level);
        // LOGGER.info("Begin to compress folder : " + sourceFolderName + " to " + outputFileName);
        addFolder(zos, sourceFolderName, sourceFolderName);
        zos.close();
        // LOGGER.info("Program ended successfully!");
    }

    private static void addFolder(ZipOutputStream zos, String folderName, String baseFolderName) throws Exception {
        File f = new File(folderName);
        if (f.exists()) {
            if (f.isDirectory()) {
                // Thank to peter
                // For pointing out missing entry for empty folder
                if (!folderName.equalsIgnoreCase(baseFolderName)) {
                    String entryName = folderName.substring(baseFolderName.length() + 1, folderName.length())
                            + File.separatorChar;
                    // System.out.println("Adding folder entry " + entryName);
                    ZipEntry ze = new ZipEntry(entryName);
                    zos.putNextEntry(ze);
                }
                File f2[] = f.listFiles();
                for (int i = 0; i < f2.length; i++) {
                    addFolder(zos, f2[i].getAbsolutePath(), baseFolderName);
                }
            } else {
                // add file
                // extract the relative name for entry purpose
                // System.out.println("baseFolderName: " + baseFolderName + " - " + baseFolderName.length());
                baseFolderName = (new File(baseFolderName)).getAbsolutePath() + File.separator;
                // System.out.println("baseFolderName: " + baseFolderName + " - " + baseFolderName.length());
                // System.out.println("folderName: " + folderName + " - " + folderName.length());
                String entryName = folderName.substring(baseFolderName.length(), folderName.length());
                long lastModified = (new File(folderName)).lastModified();
                // System.out.println("Adding file entry " + entryName + "...");
                ZipEntry ze = new ZipEntry(entryName);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(folderName);
                int len;
                byte buffer[] = new byte[1024];
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                zos.closeEntry();
                ze.setTime(lastModified);
                // LOGGER.info("OK!");
            }
        } else {
            System.out.println("Pasta não existe");
        }
    }

    public static void copyToCompact(File origem, File destiny) {
        try {
            Date date = new Date();
            InputStream in = new FileInputStream(origem);
            OutputStream out = new FileOutputStream(destiny);
            // Transferindo bytes de entrada para saída
            byte[] buffer = new byte[1024];
            int lenght;
            while ((lenght = in.read(buffer)) > 0) {
                out.write(buffer, 0, lenght);
            }
            in.close();
            out.close();
            // Long time = new Date().getTime() - date.getTime();
            // LOGGER.info("Copy to compact done, time : " + time + " !");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File[] getFilesNumbersSort(File dir) {
        File[] files = dir.listFiles();
        // ordenar usando o nome do arquivo que deve conter apenas números
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                long l1 = Long.valueOf(o1.getName());
                long l2 = Long.valueOf(o2.getName());
                if (l1 < l2) {
                    return -1;
                }
                if (l1 > l2) {
                    return 1;
                }
                return 0;
            }
        });
        return files;
    }

    public static void deleteFile(String path) {
        File delete = new File(path);
        if (delete.exists()) {
            delete.delete();
        } else {
            System.out.println("Erro ao deletar");
        }
    }

    public static void deleteFilesAtDirectory(String path) {
        try {
            FileUtils.cleanDirectory(new File(path));
        } catch (IOException e) {

        }
    }

    public static void uncompact(File zipFile, File dir) throws IOException {
        ZipFile zip = null;
        File arquivo = null;
        InputStream is = null;
        OutputStream os = null;
        byte[] buffer = new byte[1024];
        try {
            // cria diretório informado, caso não exista
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!dir.exists() || !dir.isDirectory()) {
                throw new IOException("The directory " + dir.getName() + " has no valid name!");
            }
            zip = new ZipFile(zipFile);
            Enumeration e = zip.entries();
            while (e.hasMoreElements()) {
                ZipEntry entrada = (ZipEntry) e.nextElement();
                arquivo = new File(dir, entrada.getName());
                long lastModification = entrada.getTime();
                // se for diretório inexistente, cria a estrutura e pula
                // pra próxima entrada
                if (entrada.isDirectory() && !arquivo.exists()) {
                    arquivo.mkdirs();
                    continue;
                } else {
                    // Se o arquivo já existe e a data
                }
                // se a estrutura de diretÃ³rios nÃ£o existe, cria
                if (!arquivo.getParentFile().exists()) {
                    arquivo.getParentFile().mkdirs();
                }
                // São descompactados apenas os arquivos que ainda não existem e que são mais atuais
                if (!arquivo.exists() || (arquivo.exists() && lastModification > arquivo.lastModified())) {
                    try {
                        // lÃª o arquivo do zip e grava em disco
                        is = zip.getInputStream(entrada);
                        os = new FileOutputStream(arquivo);
                        int bytesLidos = 0;
                        if (is == null) {
                            throw new ZipException("Erro ao ler a entrada do zip: " + entrada.getName());
                        }
                        while ((bytesLidos = is.read(buffer)) > 0) {
                            os.write(buffer, 0, bytesLidos);
                        }
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (Exception ex) {
                            }
                        }
                        if (os != null) {
                            try {
                                os.close();
                            } catch (Exception ex) {
                            }
                        }
                        arquivo.setLastModified(lastModification);
                    }
                }
            }
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static boolean isFilenameValid(String file) {
        File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
