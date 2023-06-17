package ru.turbogoose.servlets;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dto.*;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.services.PostService;

import java.io.Writer;

@WebServlet("/*")
public class PostServlet extends HttpServlet {
    private final PostService postService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PostServlet(PostService postService) {
        this.postService = postService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // TODO: add constraint validation
        String id = extractId(req.getPathInfo());
        try (Writer writer = resp.getWriter()) {
            resp.setContentType("application/json");
            try {
                PostDto postDto = postService.getPost(id);
                objectMapper.writeValue(writer, postDto);
                resp.setStatus(200);
            } catch (PostNotFoundException | JacksonException exc) {
                objectMapper.writeValue(writer, new ErrorDto(exc.getMessage()));
                resp.setStatus(404);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }

    private String extractId(String path) {
        return path.split("/")[1];
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (Writer writer = resp.getWriter()) {
            resp.setContentType("application/json");
            try {
                CreateDto createDto = objectMapper.readValue(req.getReader(), CreateDto.class);
                CreatedPostDto createdPostDto = postService.createPost(createDto);
                objectMapper.writeValue(writer, createdPostDto);
                resp.setStatus(201);
            } catch (JacksonException exc) {
                objectMapper.writeValue(writer, new ErrorDto(exc.getMessage()));
                resp.setStatus(400);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }
}
