package ru.turbogoose.servlets;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dao.PostDao;
import ru.turbogoose.dto.CreatePostDto;
import ru.turbogoose.dto.ErrorDto;
import ru.turbogoose.dto.PostDto;
import ru.turbogoose.exceptions.PostNotFoundException;
import ru.turbogoose.exceptions.ValidationException;
import ru.turbogoose.services.PostService;
import ru.turbogoose.utils.PathMatcher;

import java.util.Map;

@WebServlet("/api/posts/*")
public class PostServlet extends HttpServlet {
    private final Map<PathMatcher, PathHandler> PATH_MAPPINGS = Map.of(
            new PathMatcher("/{id}"), this::handleGettingById
    );
    private PostService postService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        PostDao dao = (PostDao) getServletContext().getAttribute("dao");
        postService = new PostService(dao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getPathInfo();
        for (PathMatcher matcher : PATH_MAPPINGS.keySet()) {
            if (matcher.matches(path)) {
                PathHandler handler = PATH_MAPPINGS.get(matcher);
                handler.handle(req, resp, matcher.extractVariables(path));
                return;
            }
        }
    }

    private void handleGettingById(HttpServletRequest req, HttpServletResponse resp, Map<String, String> args) {
        try {
            try {
                String id = args.get("id");
                PostDto postDto = postService.getPost(id);
                resp.setStatus(200);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getWriter(), postDto);
            } catch (ValidationException | PostNotFoundException exc) {
                exc.printStackTrace();
                resp.setStatus(400);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getWriter(), new ErrorDto(exc.getMessage()));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            try {
                CreatePostDto createPostDto = objectMapper.readValue(req.getReader(), CreatePostDto.class);
                createPostDto.validate();
                PostDto createdPostDto = postService.createPost(createPostDto);
                resp.setStatus(201);
                resp.setContentType("application/json");
                resp.setHeader("Location", generateLink(req, createdPostDto));
                objectMapper.writeValue(resp.getWriter(), createdPostDto);
            } catch (ValidationException exc) {
                exc.printStackTrace();
                resp.setStatus(400);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getWriter(), new ErrorDto(exc.getMessage()));
            } catch (JacksonException exc) {
                exc.printStackTrace();
                resp.setStatus(400);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getWriter(), new ErrorDto("Invalid JSON format"));
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
