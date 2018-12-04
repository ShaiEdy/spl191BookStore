package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

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
		super("LogisticsService");
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, c -> {
			if (c.getTickNumber() == c.getTickDuration())
			terminate();});
		subscribeEvent(DeliveryEvent.class, c -> {
			AcquireVehicleEvent acquireVehicleEvent = new AcquireVehicleEvent("LogisticsService");
			Future<DeliveryVehicle> deliveryVehicleFuture= sendEvent(acquireVehicleEvent);
			DeliveryVehicle deliveryVehicle= deliveryVehicleFuture.get();
			deliveryVehicle.deliver(c.getAddress(),c.getDistance());
		}); 
	}
}
