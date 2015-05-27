package br.grupointegrado.facebug.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe útil utilizada para criptografias, como a criptografia de senha.
 *
 * @author Antonio
 */
public class CriptografiaUtil {

    /**
     * Criptograma uma String para uma outra String.<br/>
     * Pode ser utilizado para criptografia de senhas.
     *
     * @param original
     * @return
     */
    public static String criptografarMD5(String original) {
        if (original == null || original.isEmpty()) {
            return null;
        }
        // Cria a classe com o algorítmo de criptografia
        StringBuilder criptografado = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Criptografa a String recebida
            md.update(original.getBytes());
            // Recupera os bytes já criptografados
            byte[] digest = md.digest();
            // Converte os bytes para uma String em hexadecimal
            for (byte b : digest) {
                String hexaDecimal = String.format("%02x", b & 0xff);
                criptografado.append(hexaDecimal);
            }
            System.out.println("original: " + original);
            System.out.println("criptografado: " + criptografado.toString());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        // Converte o StringBuilder para String e retorna
        return criptografado.toString();
    }
}
