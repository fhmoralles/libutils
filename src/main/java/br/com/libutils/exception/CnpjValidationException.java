package br.com.libutils.exception;

public class CnpjValidationException extends Exception {

    private static final long serialVersionUID = 1512324060203293833L;

    public CnpjValidationException() {
        super();
    }

    public CnpjValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CnpjValidationException(String message) {
        super(message);
    }

    public CnpjValidationException(Throwable cause) {
        super(cause);
    }

}
