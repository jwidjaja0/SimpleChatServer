package com.SimpleChat.Server;

import com.SimpleChat.Messages.Chat.ChatMessage;
import com.SimpleChat.Messages.Packet;
import com.SimpleChat.Messages.User.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chatroom {
    private String roomID;
    private String name;
    private String password;

    private List<User> userList;
    private Map<String, ClientConnection> activeUserMap;

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

    public void distributeMessage(ChatMessage chatMessage){
        for(User user: userList){
            String id = user.getUserID();
            Outgoing.getInstance().addToQueue(new Packet("Chat", id, chatMessage), activeUserMap.get(id));
        }
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setActiveUserMap(Map<String, ClientConnection> activeUserMap) {
        this.activeUserMap = activeUserMap;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
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
