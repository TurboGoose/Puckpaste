package ru.turbogoose.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dao.DaoSingletonFactory;
import ru.turbogoose.dto.PostDto;
import ru.turbogoose.exception.PostNotFoundException;
import ru.turbogoose.service.PostService;
import ru.turbogoose.servlet.path.TemplatePathMatcher;

import java.io.IOException;
import java.util.Map;

@WebServlet("/posts/*")
public class PostServlet extends JsonServlet {
    private final PostService postService = new PostService(DaoSingletonFactory.getInstance());

    @Override
    public void init() {
        super.init();
        addMapping(HttpMethod.GET, new TemplatePathMatcher("/{id}"), this::handlePostRetrieval);
    }

    private void handlePostRetrieval(HttpServletRequest req, HttpServletResponse resp, Map<String, String> args) throws IOException {
        try {
            String id = args.get("id");
            PostDto postDto = postService.getPost(id);
            sendResponse(resp, 200, postDto);
        } catch (PostNotFoundException exc) {
            exc.printStackTrace();
            sendError(resp, 404, exc.getMessage());
        }
    }
}
