package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    private String senderId;
    private int tickNumber;
    private int tickDuration;


    public TickBroadcast(String senderId, int tickNumber,int tickDuration) {
        this.senderId = senderId;
        this.tickNumber = tickNumber;
        this.tickDuration= tickDuration;
    }

    public int getTickNumber() {
        return tickNumber;
    }

    public int getTickDuration() {
        return tickDuration;
    }

    public String getSenderId() {
        return senderId;
    }
}
