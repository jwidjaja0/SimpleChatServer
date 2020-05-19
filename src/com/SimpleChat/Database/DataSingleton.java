package com.SimpleChat.Database;

import com.SimpleChat.Messages.Login.*;
import com.SimpleChat.Messages.Packet;

import java.sql.*;
import java.util.UUID;

public class DataSingleton {
    private static DataSingleton instance = new DataSingleton();
    private Connection connection;

    private DataSingleton(){
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

    private void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
        for(int i = 0; i < values.length; i++){
            preparedStatement.setObject(i+1, values[i]);
        }
    }

    private boolean isSignUpIDUnique(String id) throws SQLException {
        PreparedStatement prep = connection.prepareStatement(
                "select count(clientID) from userInfo where clientID = ?");
        prep.setString(1, id);
        ResultSet rs = prep.executeQuery();

        while(rs.next()){
            if(rs.getInt(1)>0){
                return false;
            }
        }
        return true;
    }

    public Packet userLogin(Packet packet){
        LoginRequest request = (LoginRequest) packet.getMessage();
        String id = "-1";
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            PreparedStatement prep = connection.prepareStatement("SELECT username, password, clientID FROM userInfo");
            ResultSet rs = prep.executeQuery();

            while(rs.next()){
                String user = rs.getString(1);
                String pw = rs.getString(2);
                if(user.equals(username) && pw.equals(password)){
                    System.out.println("Login Success");
                    id = String.valueOf(rs.getInt(3));
                    return new Packet("Login", id, new LoginSuccess());
                }
                else if(user.equals(username) && !pw.equals(password)){
                    System.out.println("Wrong password");
                    return new Packet("Login", null, new LoginFail(-2));
                }
                else{
                    System.out.println("Username doesnt exist");
                    return new Packet("Login", null, new LoginFail(-1));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Packet("Login", id, new LoginFail());
    }


    public Packet userSignUp(Packet packet){
        SignUpRequest request = (SignUpRequest)packet.getMessage();
        String username = request.getUsername();
        String id = null;
        try {
            PreparedStatement prep = connection.prepareStatement("select count(username) from userInfo where username = ?");
            setValues(prep, username);
            ResultSet rs = prep.executeQuery();

            //Check if username exist.
            while(rs.next()){
                if(rs.getInt(1) > 0){
                    System.out.println("Username already exist");
                    return new Packet("Login", id, new SignUpFail(-1));
                }
            }

            id = UUID.randomUUID().toString().substring(0,8);
            while(!isSignUpIDUnique(id)){
                id = UUID.randomUUID().toString().substring(0,8);
            }

            PreparedStatement prep1 = connection.prepareStatement(
                    "INSERT INTO userInfo(clientID, username, password, firstName, lastName, email) values(?,?,?,?,?,?)");
            setValues(prep1, id, username, request.getPassword(), request.getFirstName(), request.getLastName(), request.getEmail());
            prep1.executeUpdate();
            return new Packet("Login", id, new SignUpSuccess());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new Packet("Login", id, new SignUpFail());
    }

}
