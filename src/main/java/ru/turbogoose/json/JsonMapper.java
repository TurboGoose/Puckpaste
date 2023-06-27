package ru.turbogoose.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface JsonMapper {
    void serialize(Writer writer, Object obj) throws IOException;
    <T> T deserialize(Reader reader, Class<T> clazz) throws IOException;
}
