package com.SimpleChat.Server;

import com.SimpleChat.Database.DataSingleton;
import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Chat.JoinChatroomFail;
import com.SimpleChat.Messages.Chat.JoinChatroomRequest;
import com.SimpleChat.Messages.Chat.NewChatroomRequest;
import com.SimpleChat.Messages.Chat.NewChatroomSuccess;
import com.SimpleChat.Messages.Packet;

import java.util.List;

public class ChatHandler {
    private List<Chatroom> chatroomList;


    public ChatHandler(List<Chatroom> chatroomList) {
        this.chatroomList = chatroomList;
    }

    public void handleMessage(ServerPacket serverPacket){
        Packet packet = serverPacket.getPacket();
        String id = packet.getUserID();
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
        else if(packet.getMessage() instanceof JoinChatroomRequest){
            JoinChatroomRequest request = (JoinChatroomRequest)packet.getMessage();
            String name = request.getChatroomName();
            String password = request.getChatroomName();



            //Find chatroom by name
            int index = chatroomList.indexOf(name);
            if(index == -1){
                Packet response = new Packet("Chat", id, new JoinChatroomFail());
                Outgoing.getInstance().addToQueue(response, cc);
            }
            else{
                //TODO: add user class to add into chatroom!!
                chatroomList.get(index).insertUser(password, null);
            }
        }
    }
}
