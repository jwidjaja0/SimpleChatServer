package com.SimpleChat.Server;

import com.SimpleChat.Database.DataSingleton;
import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Chat.NewChatroomRequest;
import com.SimpleChat.Messages.Chat.NewChatroomSuccess;
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
            NewChatroomRequest request = (NewChatroomRequest)packet.getMessage();
            Packet response = DataSingleton.getInstance().insertNewChatroom(packet);

            if(response.getMessage() instanceof NewChatroomSuccess){
                NewChatroomSuccess success = (NewChatroomSuccess)response.getMessage();
                Chatroom chatroom = new Chatroom(success.getRoomID(), success.getName(), success.getPassword());
            }

            Outgoing.getInstance().addToQueue(response, cc);

            //TODO: now also put client in the new chatroom
        }
    }
}
