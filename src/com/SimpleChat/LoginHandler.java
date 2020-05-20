package com.SimpleChat;

import com.SimpleChat.Database.DataSingleton;
import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Chat.ChatMessage;
import com.SimpleChat.Messages.Interfaces.Login;
import com.SimpleChat.Messages.Login.*;
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
        //TODO: double check signuprequest method processing
        Packet packet = serverPacket.getPacket();
        if(packet.getMessage() instanceof SignUpRequest){
//            Packet response = DataSingleton.getInstance().userSignUp(packet);
            try {
//                serverPacket.getClientConnection().getObjectOutputStream().writeObject(response);
                serverPacket.getClientConnection().getObjectOutputStream().writeObject(new Packet("Login", null, new SignUpFail()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(packet.getMessage() instanceof LoginRequest){
            Packet response = DataSingleton.getInstance().userLogin(packet);
            if(response.getMessage() instanceof LoginSuccess){
                ClientConnection cc = serverPacket.getClientConnection();
                activeUserMap.put(response.getUserID(), cc);
            }

            try {
                serverPacket.getClientConnection().getObjectOutputStream().writeObject(response);
                //serverPacket.getClientConnection().getObjectOutputStream().writeObject(new Packet("Login", null, new LoginSuccess()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(packet.getMessage() instanceof LogOutRequest){
            String id = packet.getUserID();
            activeUserMap.remove(id);
        }
    }



}
