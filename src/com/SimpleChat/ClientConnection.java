package com.SimpleChat;

import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class ClientConnection implements Runnable {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private int clientNo;

    private BlockingQueue<ServerPacket> incomingQueue;
    private List<ClientConnection> clientConnectionList;
    private Thread thread;

    public ClientConnection(Socket socket, int clientNo, BlockingQueue<ServerPacket> incomingQueue, List<ClientConnection> clientConnectionList) {
        this.socket = socket;
        this.clientNo = clientNo;
        this.incomingQueue = incomingQueue;
        this.clientConnectionList = clientConnectionList;

        clientConnectionList.add(this);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("ClientConnection no: " + clientNo + " thread started");

        try{
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while(true){
                Packet packet = (Packet) objectInputStream.readObject();
                System.out.println("Received message from client type: " + packet.getMessageType());
                incomingQueue.put(new ServerPacket(this, packet));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally{
            clientConnectionList.remove(this);
        }

    }
}
