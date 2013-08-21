package br.com.libutils.exception;

public class PasswordConfirmationException extends Exception {

    private static final long serialVersionUID = 1512324060203293833L;

    public PasswordConfirmationException() {
        super();
    }

    public PasswordConfirmationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordConfirmationException(String message) {
        super(message);
    }

    public PasswordConfirmationException(Throwable cause) {
        super(cause);
    }

}
