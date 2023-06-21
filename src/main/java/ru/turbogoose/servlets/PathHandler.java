package ru.turbogoose.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@FunctionalInterface
public interface PathHandler {
    void handle(HttpServletRequest req, HttpServletResponse resp, Map<String, String> args);
}
