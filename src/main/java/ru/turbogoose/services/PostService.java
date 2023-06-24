package ru.turbogoose.services;

import ru.turbogoose.dao.PostDao;
import ru.turbogoose.dto.CreatePostDto;
import ru.turbogoose.dto.PostDto;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.mappers.PostMapper;
import ru.turbogoose.models.Post;
import ru.turbogoose.utils.FormatChecker;

public class PostService {
    private final PostMapper mapper = new PostMapper();
    private final PostDao dao;

    public PostService(PostDao dao) {
        this.dao = dao;
    }

    public PostDto createPost(CreatePostDto createPostDto) {
        Post post = mapper.toPost(createPostDto);
        dao.save(post);
        if (post.getTitle() == null) {
            String generatedTitle = generateTitle(post.getId());
            post.setTitle(generatedTitle);
            dao.update(post);
        }
        return mapper.toPostDto(post);
    }

    private String generateTitle(long id) {
        return "Post #" + id;
    }

    public PostDto getPost(String id) throws PostNotFoundException {
        if (!FormatChecker.isLongParsable(id)) {
            throw new PostNotFoundException(id);
        }
        Post post = dao.getById(Long.parseLong(id));
        return mapper.toPostDto(post);
    }
}
