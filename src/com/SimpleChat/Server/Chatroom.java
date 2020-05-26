package com.SimpleChat.Server;

public class Chatroom {
    private String roomID;
    private String name;
    private String password;

    public Chatroom(String roomID, String name, String password) {
        this.roomID = roomID;
        this.name = name;
        this.password = password;
    }

    public Chatroom(String roomID, String name){
        this(roomID,name,"");
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
