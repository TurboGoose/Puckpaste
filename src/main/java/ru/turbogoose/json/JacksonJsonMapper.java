package ru.turbogoose.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JacksonJsonMapper implements JsonMapper {
    private final ObjectMapper objectMapper;

    public JacksonJsonMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Override
    public void serialize(Writer writer, Object obj) throws IOException{
        objectMapper.writeValue(writer, obj);
    }

    @Override
    public <T> T deserialize(Reader reader, Class<T> clazz) throws IOException{
        return objectMapper.readValue(reader, clazz);
    }
}
