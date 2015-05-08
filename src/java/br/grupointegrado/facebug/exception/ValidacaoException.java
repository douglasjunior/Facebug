package br.grupointegrado.facebug.exception;

/**
 * Exceção utilizada na validação de dados
 * 
 * @author Edvaldo
 */
public class ValidacaoException extends RuntimeException {
    
    public ValidacaoException(String message) {
        super(message);
    }
}
