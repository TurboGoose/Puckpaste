package ru.turbogoose.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dao.PostDao;
import ru.turbogoose.dto.CreatePostDto;
import ru.turbogoose.dto.PostDto;
import ru.turbogoose.exception.PostNotFoundException;
import ru.turbogoose.exception.ValidationException;
import ru.turbogoose.service.PostService;
import ru.turbogoose.service.PostValidator;
import ru.turbogoose.servlet.path.PathMatcher;

import java.io.IOException;
import java.util.Map;

@WebServlet("/posts/*")
public class PostServlet extends JsonServlet {
    private PostService postService;

    @Override
    public void init() throws ServletException {
        super.init();
        PostDao dao = (PostDao) getServletContext().getAttribute("dao");
        postService = new PostService(dao);

        addGetMapping(new PathMatcher("/{id}"), this::handlePostRetrieving);
        addPostMapping(new PathMatcher("/"), this::handlePostCreation);
    }

    private void handlePostRetrieving(HttpServletRequest req, HttpServletResponse resp, Map<String, String> args)
            throws IOException {
        try {
            String id = args.get("id");
            PostDto postDto = postService.getPost(id);
            resp.setStatus(200);
            resp.setContentType("application/json");
            jsonMapper.serialize(resp.getWriter(), postDto);
        } catch (PostNotFoundException exc) {
            exc.printStackTrace();
            sendErrorMessageWithCode(resp, 404, exc.getMessage());
        }
    }

    private void handlePostCreation(HttpServletRequest req, HttpServletResponse resp, Map<String, String> args)
            throws IOException {
        try {
            CreatePostDto createPostDto = jsonMapper.deserialize(req.getReader(), CreatePostDto.class);
            PostValidator.validate(createPostDto);
            PostDto createdPostDto = postService.createPost(createPostDto);
            resp.setStatus(201);
            resp.setContentType("application/json");
            resp.setHeader("Location", generateLink(req, createdPostDto));
            jsonMapper.serialize(resp.getWriter(), createdPostDto);
        } catch (ValidationException exc) {
            exc.printStackTrace();
            sendErrorMessageWithCode(resp, 400, exc.getMessage());
        } catch (IOException exc) {
            exc.printStackTrace();
            sendErrorMessageWithCode(resp, 400, "Invalid JSON syntax");
        }
    }

    private String generateLink(HttpServletRequest req, PostDto post) {
        String scheme = req.getScheme();
        String server = req.getServerName();
        String path = req.getServletPath();
        int port = req.getServerPort();
        long id = post.getId();

        if (port == -1) {
            return String.format("%s://%s%s/%d", scheme, server, path, id);
        }
        return String.format("%s://%s:%d%s/%d", scheme, server, port, path, id);
    }
}
