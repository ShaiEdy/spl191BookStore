package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BookOrderEvent implements Event {

    private String senderName;

    public BookOrderEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
