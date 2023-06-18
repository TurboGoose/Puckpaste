package ru.turbogoose.dto;

import lombok.Data;

@Data
public class CreateDto {
    private String title;
    private String description;
    private String content;
    private int expirationTimeInDays;
}
