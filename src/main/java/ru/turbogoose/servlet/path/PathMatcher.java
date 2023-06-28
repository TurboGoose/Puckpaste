package ru.turbogoose.servlet.path;

import lombok.EqualsAndHashCode;
import ru.turbogoose.exception.MismatchException;
import ru.turbogoose.exception.PathMatcherException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@EqualsAndHashCode
public class PathMatcher {
    public static final PathMatcher EXACT_MATCHER = new PathMatcher(null);
    private final Set<String> pathVariables = new HashSet<>();
    private final Pattern pathPattern;
    private final String pathTemplate; // must starts with "/" and follow camelCase (underscores not allowed) or be null

    public PathMatcher(String pathTemplate) {
        this.pathTemplate = pathTemplate;
        validateTemplate();
        pathPattern = pathTemplateToPattern();
    }

    private void validateTemplate() {
        if (pathTemplate != null && !pathTemplate.startsWith("/")) {
            throw new PathMatcherException(
                    String.format("Path template must starts with '/', but got %s instead", pathTemplate));
        }
    }

    private Pattern pathTemplateToPattern() {
        if (pathTemplate == null) {
            return null;
        }
        try {
            Pattern pattern = Pattern.compile("\\{(.+?)}");
            Matcher matcher = pattern.matcher(pathTemplate);
            StringBuilder regexSb = new StringBuilder("^");
            while (matcher.find()) {
                String variableName = matcher.group(1);
                pathVariables.add(variableName);
                matcher.appendReplacement(regexSb, String.format("(?<%s>[^/]+)", variableName));
            }
            matcher.appendTail(regexSb);
            regexSb.append("$");
            return Pattern.compile(regexSb.toString());
        } catch (PatternSyntaxException exc) {
            throw new PathMatcherException(exc);
        }
    }

    public boolean matches(String path) {
        if (pathPattern == null) {
            return path == null;
        }
        return pathPattern.matcher(path).matches();
    }

    public Map<String, String> extractVariables(String path) {
        Map<String, String> result = new HashMap<>();

        if (pathPattern == null) {
            if (path != null) {
                throw new MismatchException(String.format("Path %s does not match template %s", path, pathTemplate));
            }
            return result;
        }

        Matcher matcher = pathPattern.matcher(path);
        if (!matcher.matches()) {
            throw new MismatchException(String.format("Path %s does not match template %s", path, pathTemplate));
        }
        for (String variable : pathVariables) {
            result.put(variable, matcher.group(variable));
        }
        return result;
    }
}
