package ru.turbogoose.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathMatcher {
    private final Set<String> pathVariables = new HashSet<>();
    private final Pattern urlPattern;

    public PathMatcher(String urlTemplate) {
        urlPattern = urlToPattern(urlTemplate);
    }

    private Pattern urlToPattern(String urlTemplate) {
        Pattern pattern = Pattern.compile("\\{(.+?)}");
        Matcher matcher = pattern.matcher(urlTemplate);
        StringBuilder regexSb = new StringBuilder("^");
        while (matcher.find()) {
            String variableName = matcher.group(1);
            pathVariables.add(variableName);
            matcher.appendReplacement(regexSb, String.format("(?<%s>[^/]+)", variableName));
        }
        matcher.appendTail(regexSb);
        regexSb.append("$");
        return Pattern.compile(regexSb.toString());
    }

    public boolean matches(String path) {
        return urlPattern.matcher(path).matches();
    }

    public Map<String, String> extractVariables(String path) {
        Map<String, String> result = new HashMap<>();
        Matcher matcher = urlPattern.matcher(path);
        if (matcher.matches()) {
            for (String variable : pathVariables) {
                result.put(variable, matcher.group(variable));
            }
        }
        return result;
    }
}
