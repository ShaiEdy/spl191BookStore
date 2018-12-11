package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class ReleaseVehicleEvent implements Event {

    private String senderName;
    private DeliveryVehicle vehicle;

    public ReleaseVehicleEvent(String senderName, DeliveryVehicle vehicle) {
        this.senderName = senderName;
        this.vehicle=vehicle;
    }

    public String getSenderName() {
        return senderName;
    }

    public DeliveryVehicle getVehicle() {
        return vehicle;
    }
}
