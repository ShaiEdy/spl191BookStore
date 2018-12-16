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
	@SuppressWarnings("unchecked")
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, c -> {
			if (c.getTickNumber() == c.getTickDuration())
			terminate();});

		subscribeEvent(DeliveryEvent.class, c -> {
			AcquireVehicleEvent acquireVehicleEvent = new AcquireVehicleEvent(getName());
			Future<Future<DeliveryVehicle>> deliveryVehicleFuture= sendEvent(acquireVehicleEvent); // We expect to get back A future object that has another future object
			//the inner future object will be resolved sometime to a vehicle.
			DeliveryVehicle deliveryVehicle = deliveryVehicleFuture.get().get(); // this is not a blocking method
			if (deliveryVehicle!=null) { //I have a vehicle
				deliveryVehicle.deliver(c.getAddress(), c.getDistance());// here it will sleep for the deliver time
				complete(c,true);
				sendEvent(new ReleaseVehicleEvent(getName(), deliveryVehicle));
			}
			else // I dont have a vehicle (inside future inside future i got null)
				complete(c,false);
		}); 
	}
}
