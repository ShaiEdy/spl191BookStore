package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.ReleaseVehicleEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

import java.util.concurrent.TimeUnit;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {

	public LogisticsService(String name) {
		super(name);
	}

	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, c -> {
			if (c.getTickNumber() == c.getTickDuration())
			terminate();});

		subscribeEvent(DeliveryEvent.class, c -> {
			AcquireVehicleEvent acquireVehicleEvent = new AcquireVehicleEvent(getName());
			Future<DeliveryVehicle> deliveryVehicleFuture= sendEvent(acquireVehicleEvent);
			DeliveryVehicle deliveryVehicle = deliveryVehicleFuture.get(100, TimeUnit.MILLISECONDS); // this is not a blocking method
			deliveryVehicle.deliver(c.getAddress(),c.getDistance());// here it will sleep for the deliver time
			sendEvent(new ReleaseVehicleEvent(getName(),deliveryVehicle));
		}); 
	}
}
