package com.leyilikeang.common.example.sqlite;

import jdk.management.resource.ResourceType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author likang
 * @date 2018/12/14 12:43
 */
public class CreateTable {

    public static void main(String[] args) {
        createNewTable();
    }

    public static void createNewTable() {
        String url = "jdbc:sqlite:D:/sqlite/java-sqlite.db";
        String sql = "CREATE TABLE IF NOT EXISTS employees (\n"
                + "id integer PRIMARY KEY,\n"
                + "name text NOT NULL,\n"
                + "capacity real\n"
                + ");";

        String view = "SELECT * FROM employees;";

        String drop = "DROP TABLE employees;";

        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("Create table finished.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
