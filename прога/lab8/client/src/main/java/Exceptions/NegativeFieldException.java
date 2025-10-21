package Exceptions;

public class NegativeFieldException extends RuntimeException {
    public NegativeFieldException(String message) {
        super(message);
    }
}