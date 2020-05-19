package com.SimpleChat;

import com.SimpleChat.Messages.Packet;

import java.util.concurrent.BlockingQueue;

public class MessageSender {
    private BlockingQueue<Packet> outgoing;
    private static MessageSender instance = new MessageSender();

    private MessageSender() {
    }

    public static MessageSender getInstance(){
        return instance;
    }

    public void setOutgoing(BlockingQueue<Packet> outgoing) {
        this.outgoing = outgoing;
    }




}
