package ru.turbogoose.utils;

import ru.turbogoose.exceptions.MismatchException;
import ru.turbogoose.exceptions.PathMatcherException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PathMatcher {
    private final Set<String> pathVariables = new HashSet<>();
    private final Pattern pathPattern;
    private final String pathTemplate;

    public PathMatcher(String pathTemplate) {
        if (pathTemplate == null || pathTemplate.isBlank()) {
            throw new PathMatcherException(String.format("Illegal path template: %s", pathTemplate));
        }
        this.pathTemplate = pathTemplate;
        pathPattern = pathTemplateToPattern(pathTemplate);
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
            throw new MismatchException(String.format("Path %s does not match to template %s", path, pathTemplate));
        }
        for (String variable : pathVariables) {
            result.put(variable, matcher.group(variable));
        }
        return result;
    }
}
