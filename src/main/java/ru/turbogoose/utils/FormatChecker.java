package ru.turbogoose.utils;

public class FormatChecker {
    public static boolean isLongParsable(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");

    }
}
