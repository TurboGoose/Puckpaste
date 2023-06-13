package ru.turbogoose.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatedPostDto {
    private String link;
    private PostDto post;
}
