package com.SimpleChat;

import com.SimpleChat.Message.ServerPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server {

    ListenNewClient listenNewClient;
    List<ClientConnection> clientConnectionList;
    BlockingQueue<ServerPacket> incomingQueue;
    List<Chatroom> chatroomList;

    public Server() {
        incomingQueue = new ArrayBlockingQueue<>(100);
        clientConnectionList = new ArrayList<>();
        chatroomList = new ArrayList<>();


        listenNewClient = new ListenNewClient(clientConnectionList, incomingQueue);
    }
}
