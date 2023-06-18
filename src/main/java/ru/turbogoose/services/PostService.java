package ru.turbogoose.services;

import ru.turbogoose.dao.PostDAO;
import ru.turbogoose.dto.CreateDto;
import ru.turbogoose.dto.PostDto;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.mappers.PostMapper;
import ru.turbogoose.models.Post;

public class PostService {
    private final PostMapper mapper = new PostMapper();
    private final PostDAO dao;

    public PostService(PostDAO dao) {
        this.dao = dao;
    }

    public PostDto createPost(CreateDto createDto) {
        Post post = mapper.toPost(createDto);
        dao.save(post);
        return mapper.toPostDto(post);
    }

    public PostDto getPost(String id) throws PostNotFoundException {
        Post post = dao.getById(Long.parseLong(id));
        return mapper.toPostDto(post);
    }
}
