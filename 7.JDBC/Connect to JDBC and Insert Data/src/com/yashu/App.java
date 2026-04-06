package com.yashu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main(String[] args) {
        try {
            // Load and register the MySQL JDBC driver.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection and auto-close resources.
            String url = "jdbc:mysql://localhost:3306/jdbclearning";
            String user = "root";
            String password = "Root@1234";

            try (Connection connect = DriverManager.getConnection(url, user, password);
                    Statement statement = connect.createStatement()) {

                // execute query
                String sql = "INSERT INTO studentinfo(id,sname,sage,scity) VALUES(3,'Sita',24,'Chennai')";
                int rowCount = statement.executeUpdate(sql);

                // process the result
                if (rowCount > 0) {
                    System.out.println("Data saved successfully");
                } else {
                    System.out.println("No changes were made");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database operation failed.");
            e.printStackTrace();
        }
    }
}
