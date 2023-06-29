package ru.turbogoose.servlet.path;

import lombok.EqualsAndHashCode;
import ru.turbogoose.exception.MismatchException;

import java.util.Collections;
import java.util.Map;

@EqualsAndHashCode
public class ExactPathMatcher implements PathMatcher {
    @Override
    public boolean matches(String path) {
        return path == null;
    }

    @Override
    public Map<String, String> extractVariables(String path) {
        if (path != null) {
            throw new MismatchException("Path does not match template exactly");
        }
        return Collections.emptyMap();
    }
}
