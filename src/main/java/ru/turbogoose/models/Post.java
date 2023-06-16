package ru.turbogoose.models;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class Post {
    private long id;
    @Builder.Default
    private String title = ""; // add automatic generation here?
    @Builder.Default
    private String description = "";
    private String content;
    @Builder.Default
    private LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastRenewedAt;

    public LocalDateTime renew(Duration duration) {
        LocalDateTime updatedTimeToLive = expiresAt.plus(duration);
        if (updatedTimeToLive.isAfter(expiresAt)) {
            lastRenewedAt = LocalDateTime.now();
            expiresAt = updatedTimeToLive;
        }
        return expiresAt;
    }
}
