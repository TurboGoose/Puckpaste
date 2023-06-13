package ru.turbogoose.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDto {
    private String title;
    private String description;
    private String content;
    private int expirationInDays;
}
