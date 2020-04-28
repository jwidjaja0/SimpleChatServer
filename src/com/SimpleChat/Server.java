package com.SimpleChat;

import java.util.List;

public class Server {

    ListenNewClient listenNewClient;
    List<ClientConnection> clientConnectionList;


    public Server() {
        listenNewClient = new ListenNewClient();
    }
}
