package ru.turbogoose.services;

import ru.turbogoose.dao.PostDAO;
import ru.turbogoose.dto.CreateDto;
import ru.turbogoose.dto.CreatedPostDto;
import ru.turbogoose.dto.PostDto;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.mappers.PostMapper;
import ru.turbogoose.models.Post;
import ru.turbogoose.utils.LinkGenerator;

public class PostService {
    private final PostMapper mapper = new PostMapper();
    private final PostDAO dao;

    public PostService(PostDAO dao) {
        this.dao = dao;
    }

    public CreatedPostDto createPost(CreateDto createDto) {
        Post post = mapper.toPost(createDto);
        dao.save(post);
        PostDto postDto = mapper.toPostDto(post);
        String link = LinkGenerator.getLink(post);
        return CreatedPostDto.builder()
                .post(postDto)
                .link(link)
                .build();
    }

    public PostDto getPost(String id) throws PostNotFoundException {
        Post post = dao.getById(Long.parseLong(id));
        return mapper.toPostDto(post);
    }
}
