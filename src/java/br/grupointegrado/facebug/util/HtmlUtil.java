/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.grupointegrado.facebug.util;

/**
 *
 * @author Felipe
 */
public class HtmlUtil {
    
    public static String xss(String texto){
        if(texto == null)
            return null;
            StringBuffer textoXSS = new StringBuffer();
            int tamanhoTexto = texto.length();
            for(int i=0; i < tamanhoTexto; i++){
                 char caracter = texto.charAt(i);
                 if (caracter == '<'){
                    textoXSS.append("&lt;");
                 } else if(caracter == '>') {
                    textoXSS.append("&gt;");
                 } else if(caracter == '&') {
                    textoXSS.append("&amp;");
                 } else if(caracter == '"') {
                    textoXSS.append("&quot;");
                 } else if(caracter == ' ') {
                    textoXSS.append("&nbsp;");
                 } else {
                    textoXSS.append(caracter);
                 }
           }
                return textoXSS.toString();
        }
    
}
