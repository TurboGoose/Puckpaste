package ru.turbogoose.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
// TODO: delete this class and pass link via Location header
public class CreatedPostDto {
    private String link;
    private PostDto post;
}
