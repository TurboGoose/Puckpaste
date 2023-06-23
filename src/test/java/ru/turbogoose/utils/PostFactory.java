package ru.turbogoose.utils;

import ru.turbogoose.models.Post;

import java.time.LocalDateTime;

public class PostFactory {
    public static Post getPostWithoutId() {
        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.setTitle("Title");
        post.setDescription("Description");
        post.setContent("Some content");
        post.setExpiresAt(now.plusDays(1));
        post.setCreatedAt(now);

        return post;
    }
}
