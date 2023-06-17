package ru.turbogoose.dao;

import org.junit.jupiter.api.Test;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.models.Post;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SqlitePostDAOTest {
    @Test
    public void testDAO() throws PostNotFoundException {
        PostDao dao = new SqlitePostDAO();

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        Post post = new Post();
        post.setTitle("Title");
        post.setDescription("Desc");
        post.setContent("Some content");
        post.setExpiresAt(now.plusDays(1));
        post.setCreatedAt(now);

        long id = dao.save(post);
        assertEquals(post, dao.getById(id));

        dao.update(post);
        assertEquals(post, dao.getById(id));

        dao.delete(id);
        assertThrows(PostNotFoundException.class, () -> dao.getById(id));
    }

}