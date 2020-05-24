package com.SimpleChat.Server;

import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Packet;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ServerSender implements Runnable {
    private BlockingQueue<ServerPacket> outgoingQueue;

    public ServerSender(BlockingQueue<ServerPacket> outgoingQueue) {
        this.outgoingQueue = outgoingQueue;
        System.out.println("Server sender started");
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(true){
            try {
                ServerPacket serverPacket = outgoingQueue.take();
                Packet packet = serverPacket.getPacket();
                ClientConnection cc = serverPacket.getClientConnection();

                cc.getObjectOutputStream().writeObject(packet);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }

    }
}
