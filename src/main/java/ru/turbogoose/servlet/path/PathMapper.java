package ru.turbogoose.servlet.path;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.exception.PathMappingNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PathMapper {
    private final Map<PathMatcher, PathHandler> mappings = new HashMap<>();

    public void addMapping(PathMatcher matcher, PathHandler handler) {
        mappings.put(matcher, handler);
    }


    public void performMapping(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        for (PathMatcher matcher : mappings.keySet()) {
            if (matcher.matches(path)) {
                PathHandler handler = mappings.get(matcher);
                handler.handle(req, resp, matcher.extractVariables(path));
                return;
            }
        }
        throw new PathMappingNotFoundException("Resource not found: " + req.getRequestURI());
    }
}
