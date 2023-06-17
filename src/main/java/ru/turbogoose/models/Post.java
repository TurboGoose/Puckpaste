package ru.turbogoose.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class Post {
    private long id;
    private String title;
    private String description;
    private String content;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt.truncatedTo(ChronoUnit.SECONDS);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.truncatedTo(ChronoUnit.SECONDS);
    }
}
