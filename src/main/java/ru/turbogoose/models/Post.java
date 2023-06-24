package ru.turbogoose.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class Post {
    private static final int DEFAULT_EXPIRATION_TIME_IN_DAYS = 7;

    private Long id;
    private String title;
    private String description;
    private String content;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public Post() {
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.expiresAt = createdAt.plusDays(DEFAULT_EXPIRATION_TIME_IN_DAYS);
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt.truncatedTo(ChronoUnit.SECONDS);
    }

    public void setExpiresAt(Integer expirationTimeInDays) {
        if (expirationTimeInDays != null) {
            this.expiresAt = createdAt.plusDays(expirationTimeInDays);
        }
    }

    // TODO: rewrite object creation in tests
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.truncatedTo(ChronoUnit.SECONDS);
    }
}
