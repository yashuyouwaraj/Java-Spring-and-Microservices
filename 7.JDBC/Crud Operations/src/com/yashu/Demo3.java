package com.yashu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.yashu.util.JavaUtil;

public class Demo3 {
    public static void main(String[] args) {
        Connection connect = null;
        Statement statement = null;

        try {
            connect = JavaUtil.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //creating statement
        try {
            statement = connect.createStatement();
        } catch (SQLException ex) {
            System.getLogger(Demo3.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        //execute query
        String sql = "Update studentinfo set sage=16 where id=1";
        boolean status;
        try {
            status = statement.execute(sql);
            if (status) {
                //select
                System.out.println("If block");
                ResultSet rs = statement.getResultSet();
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + " " + rs.getString("sname") + " "
                            + rs.getInt("sage") + " " + rs.getString("scity"));
                }
            } else {
                //insert, update, delete
                System.out.println("else block");
                int rows = statement.getUpdateCount();

                if (rows == 0) {
                    System.out.println("Operation failed");
                } else {
                    System.out.println("Operation successful");
                }
            }
        } catch (SQLException ex) {
            System.getLogger(Demo3.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        //close the resorces
        try {
            JavaUtil.closeConnection(connect, statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
