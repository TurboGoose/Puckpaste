package ru.turbogoose.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.servlet.path.PathHandler;
import ru.turbogoose.servlet.path.PathMapper;
import ru.turbogoose.servlet.path.PathMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    public enum HttpMethod {GET, POST, PUT, DELETE}

    private final Map<HttpMethod, PathMapper> mappings = new HashMap<>();

    @Override
    public void init() {
        for (HttpMethod method : HttpMethod.values()) {
            mappings.put(method, new PathMapper());
        }
    }

    public void addMapping(HttpMethod method, PathMatcher matcher, PathHandler handler) {
        PathMapper mapper = new PathMapper();
        mapper.addMapping(matcher, handler);
        mappings.put(method, mapper);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mappings.get(HttpMethod.GET).performMapping(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mappings.get(HttpMethod.POST).performMapping(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mappings.get(HttpMethod.PUT).performMapping(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mappings.get(HttpMethod.DELETE).performMapping(req, resp);
    }
}
