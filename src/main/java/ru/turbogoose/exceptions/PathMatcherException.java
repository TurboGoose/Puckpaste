package ru.turbogoose.exceptions;

public class PathMatcherException extends RuntimeException {
    public PathMatcherException() {
    }

    public PathMatcherException(String message) {
        super(message);
    }

    public PathMatcherException(Throwable cause) {
        super(cause);
    }

    public PathMatcherException(String message, Throwable cause) {
        super(message, cause);
    }
}
