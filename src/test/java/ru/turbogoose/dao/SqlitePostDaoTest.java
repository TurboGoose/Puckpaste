package ru.turbogoose.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.models.Post;
import ru.turbogoose.testutils.SqlScriptRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SqlitePostDaoTest {
    @TempDir
    static Path tempDir;
    static String url;
    PostDao dao;

    @BeforeAll
    public static void initDb() throws SQLException, IOException {
        Path dbFile = Files.createFile(tempDir.resolve("puckpaste-test.db"));
        url = "jdbc:sqlite:" + dbFile;
        SqlScriptRunner.run(url, "createTable.sql");
    }

    @BeforeEach
    public void initDao() {
        Properties props = new Properties();
        props.setProperty("url", url);
        dao = new SqlitePostDao(props);
    }

    @AfterEach
    public void rollbackDb() throws SQLException, IOException {
        SqlScriptRunner.run(url, "truncateTable.sql");
    }

    @Test
    public void testDAO() throws PostNotFoundException {
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
