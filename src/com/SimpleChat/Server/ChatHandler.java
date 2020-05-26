package com.SimpleChat.Server;

import com.SimpleChat.Database.DataSingleton;
import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Chat.*;
import com.SimpleChat.Messages.Interfaces.Chat;
import com.SimpleChat.Messages.Packet;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ChatHandler {
    //better to use hashmap, for fast lookup of chatroom
    private List<Chatroom> chatroomList;
    private Map<String, ClientConnection> activeUserMap;


    public ChatHandler(List<Chatroom> chatroomList, Map<String, ClientConnection> activeUserMap) {
        this.chatroomList = chatroomList;
        this.activeUserMap = activeUserMap;
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
                chatroom.setActiveUserMap(activeUserMap);
            }

            Outgoing.getInstance().addToQueue(response, cc);
            //TODO: now also put client in the new chatroom, client side?

        }
        else if(packet.getMessage() instanceof JoinChatroomRequest){
            JoinChatroomRequest request = (JoinChatroomRequest)packet.getMessage();
            String name = request.getChatroomName();
            String roomPassword = request.getChatroomName();

            //Find chatroom by name
            int index = chatroomList.indexOf(name);
            if(index == -1){
                Packet response = new Packet("Chat", id, new JoinChatroomFail());
                Outgoing.getInstance().addToQueue(response, cc);
            }
            else{
                //TODO: rethink data structure, maybe set is better? arraylist will require to traverse to find room
                User user = new User(packet.getUserID());
                boolean isJoined = chatroomList.get(index).insertUser(roomPassword, user);

                //if password matches
                if(isJoined){
                    //TODO: add to database, think how to store this in database

                    JoinChatroomSuccess success = new JoinChatroomSuccess();
                    Packet response = new Packet("Chat", packet.getUserID(), new JoinChatroomSuccess());
                    Outgoing.getInstance().addToQueue(response, cc);
                }
                else{
                    //TODO: modify JoinChatRoomFail to indicate fail causes. (room not exist, wrong password)
                    Packet response = new Packet("Chat", id, new JoinChatroomFail());
                    Outgoing.getInstance().addToQueue(response, cc);
                }

            }
        }
        else if(packet.getMessage() instanceof LeaveRoomRequest){
            LeaveRoomRequest request = (LeaveRoomRequest)packet.getMessage();
            String roomName = request.getRoomName();
            for(Chatroom chatroom:chatroomList){
                if(chatroom.getName().equals(roomName)){
                    for(User user: chatroom.getUserList()){
                        if(user.getUserID().equals(id)){
                            //matching user, remove, send confirmation
                            chatroom.getUserList().remove(user);
                            Packet response = makeChatPacket(id, new LeaveRoomSuccess());
                            Outgoing.getInstance().addToQueue(response, cc);
                        }
                    }
                }
            }
        }

        else if(packet.getMessage() instanceof ChatMessage){
            ChatMessage message = (ChatMessage)packet.getMessage();
            String roomName = message.getChatroomName();

            for(Chatroom ch : chatroomList){
                if(ch.getName().equals(roomName)){
                    //found correct room, propagate message to everyone in room
                    ch.distributeMessage(message);
                }
            }

        }

    }

    private Packet makeChatPacket(String id, Serializable message){
        return new Packet("Chat", id, message);
    }
}
