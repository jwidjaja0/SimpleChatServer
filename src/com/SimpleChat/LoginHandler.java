package com.SimpleChat;

import com.SimpleChat.Database.DataSingleton;
import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Chat.ChatMessage;
import com.SimpleChat.Messages.Interfaces.Login;
import com.SimpleChat.Messages.Login.LogOutRequest;
import com.SimpleChat.Messages.Login.LoginRequest;
import com.SimpleChat.Messages.Login.SignUpRequest;
import com.SimpleChat.Messages.Login.SignUpSuccess;
import com.SimpleChat.Messages.Packet;

import java.io.IOException;
import java.util.Map;

public class LoginHandler {

    private Map<String, ClientConnection> activeUserMap;

    //What about making this class a separate thread?


    public LoginHandler(Map<String, ClientConnection> activeUserMap) {
        this.activeUserMap = activeUserMap;
    }

    public void handleMessage(ServerPacket serverPacket){
        Packet packet = serverPacket.getPacket();
        if(packet.getMessage() instanceof SignUpRequest){
            Packet response = DataSingleton.getInstance().userSignUp(packet);
            try {
                serverPacket.getClientConnection().getObjectOutputStream().writeObject(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(packet.getMessage() instanceof LoginRequest){
            String id = packet.getUserID();
            ClientConnection cc = serverPacket.getClientConnection();
            activeUserMap.put(id, cc);
        }
        else if(packet.getMessage() instanceof LogOutRequest){
            String id = packet.getUserID();
            activeUserMap.remove(id);
        }
    }

    private void handleChatMessage(ServerPacket serverPacket) {
    }

    private void handleLoginMessage(ServerPacket serverPacket) {
        Packet packet = serverPacket.getPacket();
        if(packet.getMessage() instanceof LoginRequest){

        }

        if(packet.getMessage() instanceof SignUpRequest){
            Packet response = DataSingleton.getInstance().userSignUp(packet);
            try {
                serverPacket.getClientConnection().getObjectOutputStream().writeObject(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
