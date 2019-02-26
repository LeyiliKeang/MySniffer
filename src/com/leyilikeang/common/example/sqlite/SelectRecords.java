package com.leyilikeang.common.example.sqlite;

import java.security.SecureRandom;
import java.sql.*;

/**
 * @author likang
 * @date 2018/12/14 13:09
 */
public class SelectRecords {

    public static void main(String[] args) {
        SelectRecords select = new SelectRecords();
        select.selectAll();
    }

    private Connection connect() {
        String url = "jdbc:sqlite:D:/sqlite/java-sqlite.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    public void selectAll() {
        String sql = "SELECT * FROM employees";
        try {
            Connection connection = this.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("id" + "\t" + "name" + "\t" + "capacity");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + "\t"
                        + resultSet.getString("name") + "\t"
                        + resultSet.getDouble("capacity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
