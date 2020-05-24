package com.SimpleChat.Server;

import com.SimpleChat.Message.ServerPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ListenNewClient implements Runnable {
    private List<ClientConnection> clientConnectionList;
    private BlockingQueue<ServerPacket> incomingQueue;
    private int clientNo;
    private Thread thread;

    public ListenNewClient(List<ClientConnection> clientConnectionList, BlockingQueue<ServerPacket> incomingQueue) {
        this.clientConnectionList = clientConnectionList;
        this.incomingQueue = incomingQueue;
        clientNo = 0;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try{
            System.out.println("ListenNewClient thread started");
            ServerSocket serverSocket = new ServerSocket(8000);

            while(true){
                Socket socket = serverSocket.accept();
                clientNo++;
                String startingMessage = "Starting thread for client " + clientNo + " at " + new Date() + "\n";
                System.out.println(startingMessage);

                ClientConnection clientConnection = new ClientConnection(socket, clientNo, incomingQueue, clientConnectionList);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
