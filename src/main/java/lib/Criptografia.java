package lib;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


    import java.security.Key;
    import java.util.Base64;
    import javax.crypto.Cipher;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Suporte
 */
public class Criptografia {
    // Chave de criptografia = "MhlZukWsyijZwhW5oo"
    private static final String ALGORITMO = "AES/CBC/PKCS5Padding";     
    
    public static String criptografar(String mensagem) throws Exception {

        final Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, "MhlZukWsyijZwhWdsghfdjfghj!@#$!@%$##$%ÄSDFASDFasdf5oo");

        final byte[] criptografado = cipher.doFinal(mensagem.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(criptografado);
    }

    public static String descriptografar(String mensagem) throws Exception {
        
        final Cipher cipher = getCipher(Cipher.DECRYPT_MODE, "MhlZukWsyijZwhWdsghfdjfghj!@#$!@%$##$%ÄSDFASDFasdf5oo");

        final byte[] descriptografado = cipher.doFinal(Base64.getDecoder().decode(mensagem));

        return new String(descriptografado, "UTF-8");
    }

    private static Cipher getCipher(final int encryptMode, final String chave) throws Exception {

        final Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(encryptMode, buildKey(chave), new IvParameterSpec(new byte[16], 0, 16));

        return cipher;
    }

    private static Key buildKey(String chave) throws Exception {

        byte[] utf8 = chave.getBytes("UTF-8");
        final byte[] key = new byte[16];
        System.arraycopy(utf8, 0, key, 0, utf8.length < 16 ? utf8.length : 16);

        return new SecretKeySpec(key, "AES");
    }
}
