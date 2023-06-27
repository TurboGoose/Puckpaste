package ru.turbogoose.json;

public class JsonMapperFactory {
    private static JsonMapper mapper;

    public static JsonMapper getMapper() {
        if (mapper == null) {
            createMapper();
        }
        return mapper;
    }

    private static void createMapper() {
        mapper = new JacksonJsonMapper();
    }
}
