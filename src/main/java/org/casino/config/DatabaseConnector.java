package org.casino.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static Connection connection;

    public static Connection connect() {
        if (connection == null) {
            try {
                String dbPath = System.getenv("/casino.db");
                if (dbPath == null) {
                    throw new IllegalStateException("Database path environment variable not set");
                }
                String url = "jdbc:sqlite:" + dbPath;
                connection = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println("Connection to SQLite has failed.");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
