package com.leyilikeang.common.example.sqlite;

import jdk.nashorn.internal.ir.annotations.Ignore;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author likang
 * @date 2018/12/14 12:38
 */
public class CreateDB {

    public static void main(String[] args) {
        createNewDatabase("D:/sqlite/create-db.db");
    }

    public static void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        try {
            Connection connection = DriverManager.getConnection(url);
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
