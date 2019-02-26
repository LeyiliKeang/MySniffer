package com.leyilikeang.common.example.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author likang
 * @date 2018/12/14 12:31
 */
public class ConnectSQLite {

    public static void main(String[] args) {
        connect();
    }

    public static void connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:D:/sqlite/java-sqlite.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
