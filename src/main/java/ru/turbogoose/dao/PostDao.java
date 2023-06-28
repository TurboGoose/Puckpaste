package ru.turbogoose.dao;

import ru.turbogoose.exception.PostNotFoundException;
import ru.turbogoose.model.Post;

public interface PostDao {
    Post getById(long id) throws PostNotFoundException;

    long save(Post post);

    long getCount();
    boolean update(Post post);

    int deleteExpired();
}
