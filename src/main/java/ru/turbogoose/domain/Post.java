package ru.turbogoose.domain;

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
    private LocalDateTime expires = LocalDateTime.now().plusDays(7);
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastRenewedAt;

    public LocalDateTime renew(Duration duration) {
        LocalDateTime updatedTimeToLive = expires.plus(duration);
        if (updatedTimeToLive.isAfter(expires)) {
            lastRenewedAt = LocalDateTime.now();
            expires = updatedTimeToLive;
        }
        return expires;
    }
}
