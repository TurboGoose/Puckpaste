package ru.turbogoose.servlets;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dao.PostDao;
import ru.turbogoose.dao.SqlitePostDao;
import ru.turbogoose.dto.CreatePostDto;
import ru.turbogoose.dto.ErrorDto;
import ru.turbogoose.dto.PostDto;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.services.PostService;
import ru.turbogoose.utils.PropertyReader;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

@WebServlet("/")
public class PostServlet extends HttpServlet {
    private PostService postService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        try { // TODO: move this duplicated code to application startup logic
            Class.forName("org.sqlite.JDBC");
            Properties dbProps = PropertyReader.fromFile("database.properties");
            PostDao dao = new SqlitePostDao(dbProps);
            postService = new PostService(dao);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // TODO: add constraint validation
        try (Writer writer = resp.getWriter()) {
            resp.setContentType("application/json");
            String id = extractId(req.getServletPath()); // TODO: rewrite for matchers?
            if (id == null) {
                objectMapper.writeValue(writer, new ErrorDto("Resource not found"));
                resp.setStatus(404);
                return;
            }
            try {
                PostDto postDto = postService.getPost(id);
                objectMapper.writeValue(writer, postDto);
                resp.setStatus(200);
            } catch (PostNotFoundException | JacksonException exc) {
                exc.printStackTrace();
                objectMapper.writeValue(writer, new ErrorDto(exc.getMessage()));
                resp.setStatus(404);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }

    private String extractId(String path) {
        String[] split = path.split("/");
        if (split.length == 2) {
            return split[1];
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (Writer writer = resp.getWriter()) {
            resp.setContentType("application/json");
            try {
                CreatePostDto createPostDto = objectMapper.readValue(req.getReader(), CreatePostDto.class);
                PostDto createdPostDto = postService.createPost(createPostDto);
                resp.setHeader("Location", generateLink(req, createdPostDto));
                objectMapper.writeValue(writer, createdPostDto);
                resp.setStatus(201);
            } catch (JacksonException exc) {
                exc.printStackTrace();
                objectMapper.writeValue(writer, new ErrorDto(exc.getMessage()));
                resp.setStatus(400);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }

    private String generateLink(HttpServletRequest req, PostDto post) {
        String scheme = req.getScheme();
        String server = req.getServerName();
        int port = req.getServerPort();
        long id = post.getId();

        if (port == -1) {
            return String.format("%s://%s/%d", scheme, server, id);
        }
        return String.format("%s://%s:%d/%d", scheme, server, port, id);
    }
}
