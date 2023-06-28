package ru.turbogoose.servlet.path;

import java.util.Map;

public interface PathMatcher {
    boolean matches(String path);

    Map<String, String> extractVariables(String path);
}
