package com.SimpleChat.Message;

import com.SimpleChat.ClientConnection;
import com.SimpleChat.Messages.Packet;


public class ServerPacket {
    private ClientConnection clientConnection;
    private Packet packet;

    public ServerPacket(ClientConnection clientConnection, Packet packet) {
        this.clientConnection = clientConnection;
        this.packet = packet;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public Packet getPacket() {
        return packet;
    }
}