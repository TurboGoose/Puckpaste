package ru.turbogoose.exception;

public class MismatchException extends PathMatcherException {
    public MismatchException(String message) {
        super(message);
    }
}
