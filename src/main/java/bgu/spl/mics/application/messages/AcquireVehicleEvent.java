package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class AcquireVehicleEvent implements Event {

    private String senderName;

    public AcquireVehicleEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
