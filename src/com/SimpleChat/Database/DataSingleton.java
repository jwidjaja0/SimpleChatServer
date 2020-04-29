package com.SimpleChat.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSingleton {
    private static DataSingleton instance = new DataSingleton();
    private Connection connection;

    private DataSingleton(){}

    public static DataSingleton getInstance(){
        return instance;
    }

    public void setConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:SimpleChat.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
