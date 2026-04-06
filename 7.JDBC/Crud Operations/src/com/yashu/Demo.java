package com.yashu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Demo {

    public static void main(String[] args) throws Exception {

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
        String sql = "UPDATE studentinfo set sage=18 where id=2";
        int rowsCount = statement.executeUpdate(sql);

        //process the result
        if (rowsCount == 0) {
            System.out.println("Updation failed");
        } else {
            System.out.println("Update successful!");
        }

        //close the resorces
        statement.close();
        connect.close();
    }

}
