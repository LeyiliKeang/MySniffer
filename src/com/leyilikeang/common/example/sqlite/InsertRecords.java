package com.leyilikeang.common.example.sqlite;

import jdk.internal.org.objectweb.asm.TypeReference;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author likang
 * @date 2018/12/14 12:49
 */
public class InsertRecords {

    public static void main(String[] args) {
        InsertRecords records = new InsertRecords();
        records.insert("Maxsu", 30000);
        records.insert("Minsu", 40000);
        records.insert("Miswong", 50000);
        System.out.println("Insert data finished.");
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

    public void insert(String name, double capacity) {
        String sql = "INSERT INTO employees(name, capacity) VALUES(?, ?)";
        try {
            Connection connection = this.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, capacity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
