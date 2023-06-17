package ru.turbogoose.dao;

import org.junit.jupiter.api.Test;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.models.Post;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlitePostDaoTest {
    @Test
    public void testDAO() throws PostNotFoundException {
        PostDao dao = new SqlitePostDAO();

        Post post = Post.builder()
                .title("Title")
                .description("Desc")
                .content("Content")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .build();

        long id = dao.save(post);
        assertEquals(post, dao.getById(id));

        post.renew(Duration.of(10, ChronoUnit.DAYS));

        dao.update(post);
        assertEquals(post, dao.getById(id));
    }

}