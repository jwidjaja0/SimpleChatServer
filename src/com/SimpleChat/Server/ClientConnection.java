package com.SimpleChat.Server;

import com.SimpleChat.Message.ServerPacket;
import com.SimpleChat.Messages.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
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

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    @Override
    public void run() {
        System.out.println("ClientConnection no: " + clientNo + " thread started");

        try{
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            while(true){
                Packet packet = (Packet) objectInputStream.readObject();
                System.out.println("Received message from client type: " + packet.getMessage().getClass().toString());
                incomingQueue.put(new ServerPacket(this, packet));
            }
        }
        catch(IOException e){
            System.out.println("Client disconnected");
        } catch(ClassNotFoundException e){
            System.out.println("Class not found");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("client disconnect");
            e.printStackTrace();
        }
        finally{
            clientConnectionList.remove(this);
            //logout client if logged in

        }

    }
}
