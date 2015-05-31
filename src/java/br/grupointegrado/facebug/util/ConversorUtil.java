package br.grupointegrado.facebug.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe útil para conversão de tipos
 *
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

    /**
     * Converte o tipo String "dd/MM/yyyy" para o tipo java.sql.Date. <br>
     * O tipo Date garante que será gravado somente Data no banco de dados.
     *
     * @param data
     * @return
     */
    public static java.sql.Date stringParaSQLDate(String data) {
        try {
            Date date = stringParaDate(data);
            return dateParaSQLDate(date);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Converte o tipo java.util.Date para o tipo java.sql.Date. <br>
     * O tipo Date garante que será gravado somente Data no banco de dados.
     *
     * @param data
     * @return
     */
    public static java.sql.Date dateParaSQLDate(Date data) {
        try {
            return new java.sql.Date(data.getTime());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Converte o tipo java.util.Date para String no formato "dd/MM/yy HH:mm"
     * @param data
     * @return 
     */
    public static String dateTimeParaString(Date data) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
            return format.format(data);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Converte o tipo java.util.Date para o tipo java.sql.Timestamp. <br>
     * O tipo Timestamp garante que será gravado Data e Hora no banco de dados.
     *
     * @param data
     * @return
     */
    public static Timestamp dateParaTimeStamp(Date data) {
        try {
            return new Timestamp(data.getTime());
        } catch (Exception ex) {
            return null;
        }
    }
}
