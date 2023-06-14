package ru.turbogoose.dao;

import ru.turbogoose.domain.Post;
import ru.turbogoose.exceptions.PostNotFoundException;

import java.sql.*;

public class PostDao {
    private static final String URL = "jdbc:sqlite:data/puckpaste.db";

    public Post getById(long id) throws PostNotFoundException {
        String sql = "SELECT * FROM posts WHERE id=?;";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL);
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new PostNotFoundException(id);
            }
            return Post.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .content(rs.getString("content"))
                    .expires(rs.getTimestamp("expires").toLocalDateTime())
                    .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                    .lastRenewedAt(rs.getTimestamp("lastRenewedAt").toLocalDateTime())
                    .build();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean save(Post post) {
        String sql = "INSERT INTO posts(title, description, content, expires, createdAt, lastRenewedAt) values (?, ?, ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(URL);
            ps = conn.prepareStatement(sql);

            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getContent());
            ps.setTimestamp(4, Timestamp.valueOf(post.getExpires()));
            ps.setTimestamp(5, Timestamp.valueOf(post.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(post.getLastRenewedAt()));

            int affectedRowsCount = ps.executeUpdate();
            // TODO: return generated id
            return affectedRowsCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public boolean update(Post post) {
        String sql = "UPDATE posts SET title=?, description=?, content=?, expires=?, createdAt=?, lastRenewedAt=? WHERE id=?;";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(URL);
            ps = conn.prepareStatement(sql);

            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getContent());
            ps.setTimestamp(4, Timestamp.valueOf(post.getExpires()));
            ps.setTimestamp(5, Timestamp.valueOf(post.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(post.getLastRenewedAt()));
            ps.setLong(7, post.getId());

            int affectedRowsCount = ps.executeUpdate();
            return affectedRowsCount == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM posts WHERE id=?;";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(URL);
            ps = conn.prepareStatement(sql);

            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
