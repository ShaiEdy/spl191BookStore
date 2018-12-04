package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    private String senderId;
    private int tickNumber;


    public TickBroadcast(String senderId, int tickNumber) {
        this.senderId = senderId;
        this.tickNumber = tickNumber;
    }

    public int getTickNumber() {
        return tickNumber;
    }

    public String getSenderId() {
        return senderId;
    }
}
