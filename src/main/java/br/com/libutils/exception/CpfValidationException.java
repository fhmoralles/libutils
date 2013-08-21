package br.com.libutils.exception;

public class CpfValidationException extends Exception {

    private static final long serialVersionUID = 1512324060203293833L;

    public CpfValidationException() {
        super();
    }

    public CpfValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CpfValidationException(String message) {
        super(message);
    }

    public CpfValidationException(Throwable cause) {
        super(cause);
    }

}
