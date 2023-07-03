package ru.turbogoose.exception;

public class PathMappingNotFoundException extends RuntimeException {
    public PathMappingNotFoundException(String message) {
        super(message);
    }
}
