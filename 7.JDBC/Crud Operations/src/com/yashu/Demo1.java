package com.yashu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Demo1 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Load and register the MySQL JDBC driver.
        Class.forName("com.mysql.cj.jdbc.Driver");

        //Establish the connection
        String url = "jdbc:mysql://localhost:3306/jdbclearning";
        String user = "root";
        String password = "Root@1234";
        Connection connect = DriverManager.getConnection(url, user, password);

        //creating statement
        Statement statement = connect.createStatement();

        //execute query
        String sql = "SELECT * FROM studentinfo";
        ResultSet rs = statement.executeQuery(sql);

        //process the result
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " " + rs.getString("sname") + " "
                    + rs.getInt("sage") + " " + rs.getString("scity"));
        }

        //close the resorces
        rs.close();
        statement.close();
        connect.close();
    }
}
