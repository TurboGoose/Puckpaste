package ru.turbogoose.dao;

import ru.turbogoose.domain.Post;
import ru.turbogoose.exceptions.PostNotFoundException;

import java.sql.*;

public class PostDao {
    private static final String URL = "jdbc:sqlite:data/puckpaste.db";

    public Post getById(long id) throws PostNotFoundException {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String sql = "SELECT * FROM posts WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet result = ps.executeQuery();

            if (!result.next()) {
                throw new PostNotFoundException(id);
            }

            Post post = Post.builder()
                    .id(result.getLong("id"))
                    .title(result.getString("title"))
                    .description(result.getString("description"))
                    .content(result.getString("content"))
                    .expires(result.getTimestamp("expires").toLocalDateTime())
                    .createdAt(result.getTimestamp("createdAt").toLocalDateTime())
                    .lastRenewedAt(result.getTimestamp("lastRenewedAt").toLocalDateTime())
                    .build();

            return post;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Post post) {
        String sql = "INSERT INTO posts(title, description, content, expires, createdAt, lastRenewedAt) values (?, ?, ?, ?, ?, ?);";

    }

    public void update(Post post) {

    }

    public void delete(int id) {

    }
}
