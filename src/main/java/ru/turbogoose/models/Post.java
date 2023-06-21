package ru.turbogoose.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    private long id;
    private String title;
    private String description;
    private String content;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
