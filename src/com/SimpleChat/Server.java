package com.SimpleChat;

import com.SimpleChat.Database.DataSingleton;
import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server implements Runnable {
    MessageHandler messageHandler;

    ListenNewClient listenNewClient;
    List<ClientConnection> clientConnectionList;
    BlockingQueue<ServerPacket> incomingQueue;
    BlockingQueue<Packet> outgoingQueue;
    List<Chatroom> chatroomList;

    public Server() {
        incomingQueue = new ArrayBlockingQueue<>(100);
        outgoingQueue = new ArrayBlockingQueue<>(100);
        clientConnectionList = new ArrayList<>();
        chatroomList = new ArrayList<>();
        listenNewClient = new ListenNewClient(clientConnectionList, incomingQueue);
        messageHandler = new MessageHandler();

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        DataSingleton.getInstance().setConnection();

        while(true){
            try {
                ServerPacket sp = incomingQueue.take();
                messageHandler.handleMessage(sp);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
