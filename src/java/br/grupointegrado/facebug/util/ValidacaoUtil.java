package br.grupointegrado.facebug.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe util para efetuar validações de campo String, Date, int, double,
 * etc...
 *
 * @author douglas
 */
public class ValidacaoUtil {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Faz a validação de uma String retornando true ou false.<br>
     * Valida se a String não é nula e também o seu tamanho mínimo.
     *
     * @param string
     * @param tamanhoMinimo
     * @return
     */
    public static boolean validaString(String string, int tamanhoMinimo) {
        return string != null && string.trim().length() >= tamanhoMinimo;
    }

    /**
     * Faz a validação do e-mail utilizando expressão regular. <br>
     * O e-mail precisa ter de 0 a 320 carateres e respeitar o padrão:
     * ^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$
     *
     * @param email
     * @return
     */
    public static boolean validaEmail(String email) {
        if (email == null || email.isEmpty() || email.length() > 320) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
