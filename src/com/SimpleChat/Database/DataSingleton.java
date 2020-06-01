package com.SimpleChat.Database;

import com.SimpleChat.Messages.Chat.*;
import com.SimpleChat.Messages.Interfaces.Chat;
import com.SimpleChat.Messages.Login.*;
import com.SimpleChat.Messages.Packet;
import com.SimpleChat.Messages.User.UserInfo;

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
            PreparedStatement prep = connection.prepareStatement("SELECT username, password, clientID, isActive FROM userInfo");
            ResultSet rs = prep.executeQuery();

            while(rs.next()){
                String user = rs.getString(1);
                String pw = rs.getString(2);
                if(user.equals(username) && pw.equals(password)){
                    boolean isActive = rs.getBoolean(4);
                    System.out.println("Login Success");
                    id = String.valueOf(rs.getInt(3));
                    if(isActive){
                        //TODO: nickname is currently username, need to change to actual nickname
                        UserInfo userInfo = new UserInfo(user, id);
                        return new Packet("Login", id, new LoginSuccess(userInfo));
                    }
                    else{
                        return new Packet("Login", id, new LoginFail(-2));
                    }

                }

            }

            System.out.println("Wrong pw or username dont exist");
            return new Packet("Login", null, new LoginFail(-1));

        } catch (SQLException e) {
            e.printStackTrace();
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

            //Insert user to database
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

    public Packet insertNewChatroom(Packet packet){
        NewChatroomRequest request = (NewChatroomRequest)packet.getMessage();
        String name = request.getChatroomName();
        String password = request.getPassword();
        Packet response;

        try {
            //Insert new room to database
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chatrooms(roomName, password) values(?,?)");
            setValues(preparedStatement, name, password);
            int affected = preparedStatement.executeUpdate();
            System.out.println("affected: " + affected);

            //Get the room id from database
            PreparedStatement prep1 = connection.prepareStatement("SELECT id from chatrooms WHERE roomName = ?");
            prep1.setString(1, name);
            ResultSet resultSet = prep1.executeQuery();

            int roomID = -1;
            while(resultSet.next()){
                roomID = resultSet.getInt(1);
            }
            String chatRoomID = String.valueOf(roomID);
            NewChatroomSuccess success = new NewChatroomSuccess(chatRoomID, name, password);
            response = new Packet("Chat", packet.getUserID(), success);

        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            response = new Packet("Chat", packet.getUserID(), new NewChatroomFail());
        }

        return response;
    }

    public Packet insertUserToRoom(Packet packet){
        JoinChatroomRequest request = (JoinChatroomRequest)packet.getMessage();


        return null;
    }

    //TODO: Get correct roomID
    public ChatroomDetail getChatroomDetail(String roomName) {
        ChatroomDetail detail = new ChatroomDetail(roomName, "");
        return detail;
    }

    //TODO: Implement getting chatMessage properly
    public ChatMessageHistory getChatHistory(String roomName) {
        ChatMessageHistory chatMessageHistory = new ChatMessageHistory();

        return chatMessageHistory;
    }
}
