package bgu.spl.mics.application.messages;

public class CheckAvailabilityEvent {
    private String senderName;

    public CheckAvailabilityEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
