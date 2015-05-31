package br.grupointegrado.facebug.util;

/**
 * Classe útil responsável por tratamentos nas Strings que serão impressas no
 * HTML.
 *
 * @author Felipe
 */
public class HtmlUtil {

    /**
     * Faz a conversão dos caracteres reservados do HTML por seus respectivos
     * códigos.
     *
     * @param texto
     * @return
     */
    public static String xss(String texto) {
        if (texto == null) {
            return null;
        }
        StringBuilder textoXSS = new StringBuilder();
        int tamanhoTexto = texto.length();
        for (int i = 0; i < tamanhoTexto; i++) {
            char caracter = texto.charAt(i);
            if (caracter == '<') {
                textoXSS.append("&lt;");
            } else if (caracter == '>') {
                textoXSS.append("&gt;");
            } else if (caracter == '&') {
                textoXSS.append("&amp;");
            } else if (caracter == '"') {
                textoXSS.append("&quot;");
            } else if (caracter == ' ') {
                textoXSS.append("&nbsp;");
            } else {
                textoXSS.append(caracter);
            }
        }
        return textoXSS.toString();
    }

    /**
     * Substitui as quebras de linha \n por br
     *
     * @param texto
     * @return
     */
    public static String quebraLinha(String texto) {
        if (texto == null) {
            return null;
        }
        // aqui devemos utilizar o repacle(), pois o replaceAll() é para expressão regular.
        return texto.replace("\n", "<br />");
    }
}
