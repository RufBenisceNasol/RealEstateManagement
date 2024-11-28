package com.project.RealEstate.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/realestate";  // Update with your database URL
    private static final String USER = "root";  // Update with your database username
    private static final String PASSWORD = "";  // Update with your database password

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        // Ensure the connection is open and not closed
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                System.out.println("Database connection failed.");
                throw e;
            }
        }
        return connection;
    }

    // Optional: To close the connection when you're done
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection.");
            e.printStackTrace();
        }
    }
}
