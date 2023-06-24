package ru.turbogoose.utils;

import ru.turbogoose.models.Post;

public class PostFactory {
    public static Post getPostWithoutId() {
        Post post = new Post();
        post.setTitle("Title");
        post.setDescription("Description");
        post.setContent("Some content");
        post.setExpiresAt(5);
        return post;
    }
}
