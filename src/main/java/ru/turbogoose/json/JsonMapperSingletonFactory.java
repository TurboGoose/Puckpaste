package ru.turbogoose.json;

public class JsonMapperSingletonFactory {
    private static JsonMapper INSTANCE;

    public static JsonMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JacksonJsonMapper();
        }
        return INSTANCE;
    }
}
