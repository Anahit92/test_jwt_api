package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataAccess {

    private static final String DB_NAME = "postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String HOSTNAME = "pgdb";
    private static final String PORT = "5432";
    private static final String JDBC_URL = "jdbc:postgresql://" + HOSTNAME + ":" + PORT + "/" + DB_NAME + "?user=" + USERNAME + "&password=" + PASSWORD + "&ssl=false";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL);
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        return null;
    }
}