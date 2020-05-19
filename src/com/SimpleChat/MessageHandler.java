package com.SimpleChat;

import com.SimpleChat.Database.DataSingleton;
import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Chat.ChatMessage;
import com.SimpleChat.Messages.Interfaces.Login;
import com.SimpleChat.Messages.Login.SignUpRequest;
import com.SimpleChat.Messages.Packet;

import java.io.IOException;

public class MessageHandler {

    public MessageHandler(){};

    public void handleMessage(ServerPacket serverPacket){
        Packet packet = serverPacket.getPacket();
        if(packet.getMessage() instanceof Login){
            handleLoginMessage(serverPacket);
        }
        else if(packet.getMessage() instanceof ChatMessage){
            handleChatMessage(serverPacket);
        }
    }

    private void handleChatMessage(ServerPacket serverPacket) {
    }

    private void handleLoginMessage(ServerPacket serverPacket) {
        Packet packet = serverPacket.getPacket();
        if(packet.getMessage() instanceof SignUpRequest){

            Packet response = DataSingleton.getInstance().userSignUp(packet);
            String id = response.getUserID();
            try {
                serverPacket.getClientConnection().getObjectOutputStream().writeObject(new Packet("Login", id, response));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
