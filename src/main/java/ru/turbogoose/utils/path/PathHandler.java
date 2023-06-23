package ru.turbogoose.utils.path;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@FunctionalInterface
public interface PathHandler {
    void handle(HttpServletRequest req, HttpServletResponse resp, Map<String, String> args);
}
