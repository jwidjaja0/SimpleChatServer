package com.SimpleChat.Server;

import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Chat.NewChatroomRequest;
import com.SimpleChat.Messages.Packet;

import java.util.List;

public class ChatHandler {
    private List<Chatroom> chatroomList;

    public ChatHandler() {
    }

    public void handleMessage(ServerPacket serverPacket){
        Packet packet = serverPacket.getPacket();
        ClientConnection cc = serverPacket.getClientConnection();
        if(packet.getMessage() instanceof NewChatroomRequest){

        }
    }
}
