package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BookOrderEvent implements Event {

    private String senderName;
    private String bookTitle;

    public BookOrderEvent(String senderName, String bookTitle) {
        this.senderName = senderName;
        this.bookTitle = bookTitle;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
