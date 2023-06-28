package ru.turbogoose.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    public static Properties fromFile(String filename) throws IOException {
        Properties props = new Properties();
        try (InputStream is = PropertyReader.class.getClassLoader().getResourceAsStream(filename)) {
            props.load(is);
        }
        return props;
    }
}
