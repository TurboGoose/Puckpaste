package ru.turbogoose.dao;

import java.util.Properties;

public class DaoFactory {
    public static PostDao getPostDao(Properties props) {
        return new SqlitePostDao(props);
    }
}
