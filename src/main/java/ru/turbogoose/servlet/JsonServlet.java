package ru.turbogoose.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dto.ErrorDto;
import ru.turbogoose.json.JsonMapper;
import ru.turbogoose.json.JsonMapperFactory;
import ru.turbogoose.servlet.path.PathHandler;
import ru.turbogoose.servlet.path.PathMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/*")
public class JsonServlet extends HttpServlet {
    protected final JsonMapper jsonMapper = JsonMapperFactory.getMapper();
    private final Map<PathMatcher, PathHandler> getMappings = new HashMap<>();
    private final Map<PathMatcher, PathHandler> postMappings = new HashMap<>();

    public void addGetMapping(PathMatcher matcher, PathHandler handler) {
        getMappings.put(matcher, handler);
    }

    public void addPostMapping(PathMatcher matcher, PathHandler handler) {
        postMappings.put(matcher, handler);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        performMapping(getMappings, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        performMapping(postMappings, req, resp);
    }

    private void performMapping(Map<PathMatcher, PathHandler> mappings, HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = req.getPathInfo();
            for (PathMatcher matcher : mappings.keySet()) {
                if (matcher.matches(path)) {
                    PathHandler handler = mappings.get(matcher);
                    handler.handle(req, resp, matcher.extractVariables(path));
                    return;
                }
            }
            sendErrorMessageWithCode(resp, 404, "Resource not found " + req.getRequestURI());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }

    protected void sendErrorMessageWithCode(HttpServletResponse resp, int code, String message) throws IOException {
        resp.setStatus(code);
        resp.setContentType("application/json");
        jsonMapper.serialize(resp.getWriter(), new ErrorDto(message));
    }
}
