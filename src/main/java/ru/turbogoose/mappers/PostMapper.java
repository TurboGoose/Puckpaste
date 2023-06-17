package ru.turbogoose.mappers;

import ru.turbogoose.models.Post;
import ru.turbogoose.dto.CreateDto;
import ru.turbogoose.dto.PostDto;

import java.time.LocalDateTime;

public class PostMapper {
    public PostDto toPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .expiresAt(post.getExpiresAt())
                .build();
    }

    public Post toPost(CreateDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setContent(dto.getContent());
        post.setExpiresAt(LocalDateTime.now().plusDays(dto.getExpirationInDays()));
        return post;
    }
}
