package ru.turbogoose.exception;

public class PathMatcherException extends RuntimeException {
    public PathMatcherException(String message) {
        super(message);
    }

    public PathMatcherException(Throwable cause) {
        super(cause);
    }
}
