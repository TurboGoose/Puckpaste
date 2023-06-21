package ru.turbogoose.exceptions;

public class MismatchException extends PathMatcherException {
    public MismatchException() {
    }

    public MismatchException(String message) {
        super(message);
    }

    public MismatchException(Throwable cause) {
        super(cause);
    }

    public MismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
