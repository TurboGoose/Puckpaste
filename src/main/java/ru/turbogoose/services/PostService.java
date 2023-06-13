package ru.turbogoose.services;

import ru.turbogoose.dto.*;
import ru.turbogoose.exceptions.PostNotExistsException;

public class PostService {
    public CreatedPostDto createPost(CreateDto createDto) {
        return null;
    }

    public PostDto getPost(String id) throws PostNotExistsException {
        return null;
    }

    public RenewedPostDto renewPost(String id, RenewDto renewDto) throws PostNotExistsException {
        return null;
    }
}
