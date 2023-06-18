package ru.turbogoose.dto;

import lombok.Data;

@Data
// TODO: delete this class and pass link via Location header
public class CreatedPostDto {
    private String link;
    private PostDto post;
}
