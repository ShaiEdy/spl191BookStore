package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;

public class BookOrderEvent implements Event {

    private String senderName;
    private String bookTitle;
    private Customer customer;
    private int tickNumber;

    public BookOrderEvent(String senderName, String bookTitle, Customer customer, int tickNumber) {
        this.senderName = senderName;
        this.bookTitle = bookTitle;
        this.customer = customer;
        this.tickNumber = tickNumber;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getOrderTick() {
        return tickNumber;
    }
}
