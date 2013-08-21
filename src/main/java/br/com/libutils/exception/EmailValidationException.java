package br.com.libutils.exception;

public class EmailValidationException extends Exception {

    private static final long serialVersionUID = 1512324060203293833L;

    public EmailValidationException() {
        super();
    }

    public EmailValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailValidationException(String message) {
        super(message);
    }

    public EmailValidationException(Throwable cause) {
        super(cause);
    }

}
