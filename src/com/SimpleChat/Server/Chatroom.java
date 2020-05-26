package com.SimpleChat.Server;

import com.SimpleChat.Messages.User.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class Chatroom {
    private String roomID;
    private String name;
    private String password;

    private List<User> userList;

    public Chatroom(String roomID, String name, String password) {
        this.roomID = roomID;
        this.name = name;
        this.password = password;

        userList = new ArrayList<>();
    }

    public Chatroom(String roomID, String name){
        this(roomID,name,"");
    }

    public boolean insertUser(String pw, User user){
        if(!pw.equals(password)){
            return false;
        }
        else{
            userList.add(user);
            return true;
        }
    }


    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
