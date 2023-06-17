package ru.turbogoose.dao;

import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.models.Post;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SqlitePostDAO implements PostDao {
    private static final String URL = "jdbc:sqlite:/Users/ilakonovalov/Sqlite/puckpaste.db";
    // read it from properties or mb inject? Because same format used in models and dtos
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    @Override
    public Post getById(long id) throws PostNotFoundException {
        String sql = "SELECT * FROM posts WHERE id=?;";
        try (Connection conn = DriverManager.getConnection(URL);
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
            post.setExpiresAt(LocalDateTime.parse(rs.getString("expires_at"), formatter));
            post.setCreatedAt(LocalDateTime.parse(rs.getString("created_at"), formatter));
            post.setLastRenewedAt(LocalDateTime.parse(rs.getString("last_renewed_at"), formatter));
            return post;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long save(Post post) {
        String sql = "INSERT INTO posts(title, description, content, expires_at, created_at, last_renewed_at)" +
                " values (?, ?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getContent());
            ps.setString(4, formatter.format(post.getExpiresAt()));
            ps.setString(5, formatter.format(post.getCreatedAt()));
            ps.setString(6, formatter.format(post.getLastRenewedAt()));

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
        String sql = "UPDATE posts SET title=?, description=?, content=?, expires_at=?, created_at=?, last_renewed_at=? WHERE id=?;";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getContent());
            ps.setString(4, formatter.format(post.getExpiresAt()));
            ps.setString(5, formatter.format(post.getCreatedAt()));
            ps.setString(6, formatter.format(post.getLastRenewedAt()));
            ps.setLong(7, post.getId());

            int affectedRowsCount = ps.executeUpdate();
            return affectedRowsCount == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM posts WHERE id=?;";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
