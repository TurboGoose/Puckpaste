package ru.turbogoose.exceptions;

public class PostNotFoundException extends Exception {
    private final String targetId;

    public PostNotFoundException(String targetId) {
        this.targetId = targetId;
    }

    public PostNotFoundException(long targetId) {
        this.targetId = Long.toString(targetId);
    }

    @Override
    public String getMessage() {
        return String.format("Post with id '%s' not found", targetId);
    }
}
