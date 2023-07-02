package ru.turbogoose.dao;

import java.util.Properties;

public class DaoSingletonFactory {
    private static PostDao INSTANCE;

    public static void init(Properties props) {
        if (INSTANCE == null) {
            INSTANCE = new SqlitePostDao(props);
        }
    }

    public static PostDao getInstance() {
        return INSTANCE;
    }
}
