package ru.turbogoose.dto;

import lombok.Data;

@Data
public class CreatePostDto {
    private String title;
    private String description;
    private String content;
    private int expirationTimeInDays;
}
