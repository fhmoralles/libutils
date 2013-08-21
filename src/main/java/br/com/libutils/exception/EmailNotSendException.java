package br.com.libutils.exception;

public class EmailNotSendException extends RuntimeException {

    private static final long serialVersionUID = 1512324060203293833L;

    public EmailNotSendException() {
        super();
    }

    public EmailNotSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotSendException(String message) {
        super(message);
    }

    public EmailNotSendException(Throwable cause) {
        super(cause);
    }

}
