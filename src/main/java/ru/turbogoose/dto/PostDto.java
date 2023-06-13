package ru.turbogoose.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostDto {
    private long id;
    private String title;
    private String description;
    private String content;
    private LocalDateTime expires;
}
