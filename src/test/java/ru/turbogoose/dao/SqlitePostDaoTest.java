package ru.turbogoose.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.models.Post;
import ru.turbogoose.testutils.SqlScriptRunner;
import ru.turbogoose.utils.PostFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
    public void whenCreatePostAndGetByIdThenCreateAndReturn() throws PostNotFoundException {
        Post post = PostFactory.getPostWithoutId();
        long id = dao.save(post);
        assertThat(post, is(dao.getById(id)));
    }

    @Test
    public void whenRetrievingPostByNonExistentIdThenThrow() {
        assertThrows(PostNotFoundException.class, () -> dao.getById(55));
    }

    @ParameterizedTest
    @MethodSource("getPostsWhichViolateConstraints")
    public void whenCreatePostWithConstraintViolationThenThrow(Post post) {
        assertThrows(RuntimeException.class, () -> dao.save(post));
    }

    private static Stream<Arguments> getPostsWhichViolateConstraints() {
        Post tooLongTitle = PostFactory.getPostWithoutId();
        tooLongTitle.setTitle("t".repeat(101));

        Post tooLongDescription = PostFactory.getPostWithoutId();
        tooLongDescription.setDescription("d".repeat(1501));

        Post tooLongContent = PostFactory.getPostWithoutId();
        tooLongContent.setContent("c".repeat(20001));

        return Stream.of(
                Arguments.of(tooLongTitle),
                Arguments.of(tooLongDescription),
                Arguments.of(tooLongContent)
        );
    }

    @Test
    public void whenGetPostCountThenReturnCount() {
        assertThat(dao.getCount(), is(0L));
        dao.save(PostFactory.getPostWithoutId());
        dao.save(PostFactory.getPostWithoutId());
        assertThat(dao.getCount(), is(2L));
    }

    @Test
    public void whenUpdatePostWithExistentIdThenUpdateAndReturnTrue() throws PostNotFoundException {
        Post post = PostFactory.getPostWithoutId();
        dao.save(post);

        LocalDateTime now = LocalDateTime.now();
        post.setTitle("New title");
        post.setDescription("New desc");
        post.setContent("New content");
        post.setCreatedAt(now.plusDays(1));
        post.setExpiresAt(now.plusDays(3));
        assertThat(dao.update(post), is(true));

        Post updated = dao.getById(post.getId());
        assertThat(updated, is(post));
    }

    @Test
    public void whenUpdatePostWithNonExistentIdThenReturnFalse() {
        Post post = PostFactory.getPostWithoutId();
        post.setId(1000L);
        assertThat(dao.update(post), is(false));
    }

    @Test
    public void whenDeleteExpiredPostsThenDeleteExpiredOnly() throws PostNotFoundException {
        LocalDateTime now = LocalDateTime.now();

        Post expired = PostFactory.getPostWithoutId();
        expired.setExpiresAt(now.minusDays(1));


        Post notExpired = PostFactory.getPostWithoutId();
        notExpired.setExpiresAt(now.plusDays(1));

        dao.save(expired);
        dao.save(notExpired);

        assertThat(dao.deleteExpired(), is(1));
        assertThat(dao.getById(notExpired.getId()), is(notExpired));
        assertThrows(PostNotFoundException.class, () -> dao.getById(expired.getId()));
    }
}
