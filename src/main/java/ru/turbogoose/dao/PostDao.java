package ru.turbogoose.dao;

import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.models.Post;

public interface PostDao {
    Post getById(long id) throws PostNotFoundException;

    long save(Post post);

    long getCount();

    int deleteExpired();
}
