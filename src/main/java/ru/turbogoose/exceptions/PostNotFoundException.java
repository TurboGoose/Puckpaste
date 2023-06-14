package ru.turbogoose.exceptions;

public class PostNotFoundException extends Exception {
    private long targetId;

    public PostNotFoundException(long targetId) {
        this.targetId = targetId;
    }

    @Override
    public String getMessage() {
        return String.format("Post with id %d not found", targetId);
    }
}
