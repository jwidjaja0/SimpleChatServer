package com.SimpleChat.Server;

import com.SimpleChat.Database.DataSingleton;
import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Chat.ChatMessage;
import com.SimpleChat.Messages.Interfaces.Login;
import com.SimpleChat.Messages.Packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server implements Runnable {
    private LoginHandler loginHandler;

    private ListenNewClient listenNewClient;
    private List<ClientConnection> clientConnectionList;
    private BlockingQueue<ServerPacket> incomingQueue;
    private BlockingQueue<ServerPacket> outgoingQueue;
    private List<Chatroom> chatroomList;

    private Map<String, ClientConnection> activeUserMap;
    private ServerSender serverSender;

    public Server() {
        incomingQueue = new ArrayBlockingQueue<>(100);
        outgoingQueue = new ArrayBlockingQueue<>(100);
        clientConnectionList = new ArrayList<>();
        chatroomList = new ArrayList<>();
        listenNewClient = new ListenNewClient(clientConnectionList, incomingQueue);
        activeUserMap = new HashMap<>();
        loginHandler = new LoginHandler(activeUserMap);

        serverSender = new ServerSender(outgoingQueue);
        Outgoing.getInstance().setOutgoingQueue(outgoingQueue);

        Thread thread = new Thread(this);
        thread.start();
    }

    private void messageFilter(ServerPacket serverPacket){
        Packet packet = serverPacket.getPacket();
        if(packet.getMessage() instanceof Login){
            loginHandler.handleMessage(serverPacket);
        }
        else if(packet.getMessage() instanceof ChatMessage){

        }
    }

    @Override
    public void run() {
        DataSingleton.getInstance().setConnection();

        while(true){
            try {
                ServerPacket sp = incomingQueue.take();
                messageFilter(sp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
