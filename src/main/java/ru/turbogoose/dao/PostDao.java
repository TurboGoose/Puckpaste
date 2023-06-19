package ru.turbogoose.dao;

import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.models.Post;

public interface PostDao {
    Post getById(long id) throws PostNotFoundException;

    long save(Post post);

    boolean update(Post post);

    void delete(long id);

    long getCount();

    int deleteExpired();
}
