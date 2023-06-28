package ru.turbogoose.servlet.path;

import lombok.EqualsAndHashCode;
import ru.turbogoose.exception.MismatchException;
import ru.turbogoose.exception.PathMatcherException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@EqualsAndHashCode
public class TemplatePathMatcher implements PathMatcher {
    private final Set<String> pathVariables = new HashSet<>();
    private final Pattern pathPattern;
    private final String pathTemplate;

    public TemplatePathMatcher(String pathTemplate) {
        validatePathTemplate(pathTemplate);
        this.pathTemplate = pathTemplate;
        pathPattern = pathTemplateToPattern(pathTemplate);
    }

    private void validatePathTemplate(String pathTemplate) {
        if (pathTemplate == null) {
            throw new PathMatcherException("Path template must not be null");
        }
        if (pathTemplate.isBlank() || !pathTemplate.startsWith("/")) {
            throw new PathMatcherException(String.format("Path template must starts with '/' (got '%s')", pathTemplate));
        }
    }

    private Pattern pathTemplateToPattern(String pathTemplate) {
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
        return path != null && pathPattern.matcher(path).matches();
    }

    public Map<String, String> extractVariables(String path) {
        Map<String, String> result = new HashMap<>();
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
