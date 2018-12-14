package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.ReleaseVehicleEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourceHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{

	private ResourcesHolder resourcesHolder ;
	private LinkedBlockingQueue<Future<DeliveryVehicle>> futureVehicleQueue;

	public ResourceService(String name) {
		super(name);
		resourcesHolder = ResourcesHolder.getInstance();
		futureVehicleQueue= new LinkedBlockingQueue<>();

	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, c -> {
			if (c.getTickNumber() == c.getTickDuration()) {
				for (Future future : futureVehicleQueue) {
					if (!future.isDone()) future.resolve(null);  // when unregister- we want to put null in the futures that wait to be resolve
				}
				terminate();
			}
		});
		subscribeEvent(AcquireVehicleEvent.class, c -> {
			Future<DeliveryVehicle> future = resourcesHolder.acquireVehicle();
			if (!future.isDone()) futureVehicleQueue.offer(future);// if the object inside future is null- it mean that it wait to get a car- we put it in the waiting list
			complete(c, future);
		});
		subscribeEvent(ReleaseVehicleEvent.class, c -> resourcesHolder.releaseVehicle(c.getVehicle()));
	}
}
