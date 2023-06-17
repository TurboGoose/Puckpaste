package ru.turbogoose.models;

import lombok.Data;

import java.time.Duration;
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
    private LocalDateTime lastRenewedAt;

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt.truncatedTo(ChronoUnit.SECONDS);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.truncatedTo(ChronoUnit.SECONDS);
    }

    public void setLastRenewedAt(LocalDateTime lastRenewedAt) {
        this.lastRenewedAt = lastRenewedAt.truncatedTo(ChronoUnit.SECONDS);
    }

    public LocalDateTime renew(Duration duration) {
        LocalDateTime updatedExpirationTime = expiresAt.plus(duration);
        setLastRenewedAt(LocalDateTime.now());
        setExpiresAt(updatedExpirationTime);
        return expiresAt;
    }
}
