package com.SimpleChat.Database;

import com.SimpleChat.Messages.Login.LoginRequest;
import com.SimpleChat.Messages.Login.LoginResponse;
import com.SimpleChat.Messages.Packet;

import java.sql.*;

public class DataSingleton {
    private static DataSingleton instance = new DataSingleton();
    private Connection connection;

    private DataSingleton(){
        setConnection();
    }

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

    public Packet userLogin(Packet packet){
        LoginRequest request = (LoginRequest) packet.getMessage();
        String id = "-1";
        String username = request.getUsername();
        String password = request.getPassword();
        LoginResponse response = null;

        try {
            PreparedStatement prep = connection.prepareStatement("SELECT username, password, clientID FROM userInfo");
            ResultSet rs = prep.executeQuery();

            while(rs.next()){
                String user = rs.getString(1);
                String pw = rs.getString(2);
                if(user.equals(username) && pw.equals(password)){
                    System.out.println("Login Success");
                    id = String.valueOf(rs.getInt(3));
                    response = new LoginResponse(true);
                }
                else if(user.equals(username) && !pw.equals(password)){
                    System.out.println("Wrong password");
                    response = new LoginResponse(false, true);
                }
                else{
                    System.out.println("Username doesnt exist");
                    response = new LoginResponse(false, false);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Packet("Login", id, response);
    }


}
