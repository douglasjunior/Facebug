package br.grupointegrado.facebug.util;

/**
 *
 * @author Felipe
 */
public class HtmlUtil {

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

    public static String quebraLinha(String texto) {
        if (texto == null) {
            return null;
        }
       return texto.replaceAll("\n", "<br />");
       
    }
}
