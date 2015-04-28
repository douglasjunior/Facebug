package br.grupointegrado.facebug.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe útil para conversão de tipos
 * @author Douglas
 */
public class ConversorUtil {

    /**
     * Converte uma String para Integer
     *
     * @param valor
     * @return
     */
    public static int stringParaInteger(String valor) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Converte uma String para Double
     *
     * @param valor
     * @return
     */
    public static double stringParaDouble(String valor) {
        try {
            return Double.parseDouble(valor.trim());
        } catch (Exception ex) {
            return 0.0;
        }
    }

    /**
     * Converte para Date uma String no formato "dd/MM/yyyy"
     *
     * @param data
     * @return
     */
    public static Date stringParaDate(String data) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.parse(data);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Converte para String um Date no formato "dd/MM/yyyy"
     *
     * @param data
     * @return
     */
    public static String dateParaString(Date data) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(data);
        } catch (Exception ex) {
            return "";
        }
    }

    public static java.sql.Date stringParaSQLDate(String data) {
        try {
            Date date = stringParaDate(data);
            return dateParaSQLDate(date);
        } catch (Exception ex) {
            return null;
        }
    }

    public static java.sql.Date dateParaSQLDate(Date date) {
        try {
            return new java.sql.Date(date.getTime());
        } catch (Exception ex) {
            return null;
        }
    }
}
