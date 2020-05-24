package com.SimpleChat.Server;

import com.SimpleChat.Database.DataSingleton;
import com.SimpleChat.Message.ServerPacket;
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
        ClientConnection cc = serverPacket.getClientConnection();
        if(packet.getMessage() instanceof SignUpRequest){
            Packet response = DataSingleton.getInstance().userSignUp(packet);
            Outgoing.getInstance().addToQueue(response, cc);
//            try {
//                serverPacket.getClientConnection().getObjectOutputStream().writeObject(response);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        else if(packet.getMessage() instanceof LoginRequest){
            Packet response = DataSingleton.getInstance().userLogin(packet);
            if(response.getMessage() instanceof LoginSuccess){
                activeUserMap.put(response.getUserID(), cc);
                Outgoing.getInstance().addToQueue(response, cc);
            }
//
//            try {
//                serverPacket.getClientConnection().getObjectOutputStream().writeObject(response);
//                //serverPacket.getClientConnection().getObjectOutputStream().writeObject(new Packet("Login", null, new LoginSuccess()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        else if(packet.getMessage() instanceof LogOutRequest){
            System.out.println("Removing user from logged in list");
            String id = packet.getUserID();
            activeUserMap.remove(id);
        }
    }



}
