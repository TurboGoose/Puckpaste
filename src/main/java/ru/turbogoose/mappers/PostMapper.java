package ru.turbogoose.mappers;

import ru.turbogoose.models.Post;
import ru.turbogoose.dto.CreateDto;
import ru.turbogoose.dto.PostDto;

import java.time.LocalDateTime;

public class PostMapper {
    public PostDto toPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        postDto.setExpiresAt(post.getExpiresAt());
        return postDto;
    }

    public Post toPost(CreateDto dto) {
        LocalDateTime now = LocalDateTime.now();
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setContent(dto.getContent());
        post.setCreatedAt(now);
        post.setExpiresAt(now.plusDays(dto.getExpirationTimeInDays()));
        return post;
    }
}
