package ru.turbogoose.testutils;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlScriptRunner {
    public static void run(String url, String filename) throws IOException, SQLException {
        String sql = readSqlFromFile(filename);
        try (Connection conn = DriverManager.getConnection(url);
             Statement st = conn.createStatement()) {
                st.execute(sql);
        }
    }

    private static String readSqlFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        SqlScriptRunner.class.getClassLoader().getResourceAsStream(filename)))) {
            StringBuilder result = new StringBuilder();
            reader.lines().forEach(result::append);
            return result.toString();
        }
    }
}
