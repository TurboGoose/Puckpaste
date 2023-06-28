package ru.turbogoose.mapper;

import ru.turbogoose.dto.CreatePostDto;
import ru.turbogoose.dto.PostDto;
import ru.turbogoose.model.Post;

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

    public Post toPost(CreatePostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setContent(dto.getContent());
        post.setExpiresAt(dto.getExpirationTimeInDays());
        return post;
    }
}
