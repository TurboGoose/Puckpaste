package ru.turbogoose.dao;

import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.models.Post;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class SqlitePostDAO implements PostDAO {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final String url;

    public SqlitePostDAO(Properties props) {
        this.url = props.getProperty("url");
    }

    @Override
    public Post getById(long id) throws PostNotFoundException {
        String sql = "SELECT * FROM posts WHERE id=?;";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new PostNotFoundException(id);
            }

            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setDescription(rs.getString("description"));
            post.setContent(rs.getString("content"));
            post.setExpiresAt(LocalDateTime.parse(rs.getString("expires_at"), FORMATTER));
            post.setCreatedAt(LocalDateTime.parse(rs.getString("created_at"), FORMATTER));
            return post;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long save(Post post) {
        String sql = "INSERT INTO posts(title, description, content, expires_at, created_at) values (?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getContent());
            ps.setString(4, FORMATTER.format(post.getExpiresAt()));
            ps.setString(5, FORMATTER.format(post.getCreatedAt()));

            int affectedRowsCount = ps.executeUpdate();
            if (affectedRowsCount == 0) {
                throw new SQLException("Saving post failed");
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Key was not generated");
            }

            long generatedId = rs.getLong(1);
            post.setId(generatedId);
            return generatedId;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Post post) {
        String sql = "UPDATE posts SET title=?, description=?, content=?, expires_at=?, created_at=? WHERE id=?;";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getContent());
            ps.setString(4, FORMATTER.format(post.getExpiresAt()));
            ps.setString(5, FORMATTER.format(post.getCreatedAt()));
            ps.setLong(6, post.getId());

            int affectedRowsCount = ps.executeUpdate();
            return affectedRowsCount == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM posts WHERE id=?;";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
