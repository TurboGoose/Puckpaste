package ru.turbogoose.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dao.DaoSingletonFactory;
import ru.turbogoose.dto.CreatePostDto;
import ru.turbogoose.dto.PostDto;
import ru.turbogoose.exception.ValidationException;
import ru.turbogoose.service.PostService;
import ru.turbogoose.service.PostValidator;

import java.io.IOException;

@WebServlet("/posts")
public class PostCreationServlet extends JsonServlet {
    private final PostService postService = new PostService(DaoSingletonFactory.getInstance());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CreatePostDto createPostDto = jsonMapper.deserialize(req.getReader(), CreatePostDto.class);
            PostValidator.validate(createPostDto);
            PostDto createdPostDto = postService.createPost(createPostDto);
            resp.setHeader("Location", generateLink(req, createdPostDto));
            sendResponse(resp, 201, createdPostDto);
        } catch (ValidationException exc) {
            exc.printStackTrace();
            sendError(resp, 400, exc.getMessage());
        } catch (IOException exc) {
            exc.printStackTrace();
            sendError(resp, 400, "Invalid JSON syntax");
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
