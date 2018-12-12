package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.ReleaseVehicleEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

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

	private ResourcesHolder resourcesHolder = ResourcesHolder.getInstance();

	public ResourceService(String name) {
		super(name);
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, c -> {
			if (c.getTickNumber() == c.getTickDuration())
				terminate();
		});
		subscribeEvent(AcquireVehicleEvent.class, new Callback<AcquireVehicleEvent>() {
			public void call(AcquireVehicleEvent c) {
				Future future = resourcesHolder.acquireVehicle();
				complete(c, future.get());
			}
		});
		subscribeEvent(ReleaseVehicleEvent.class, new Callback<ReleaseVehicleEvent>() {
			public void call(ReleaseVehicleEvent c) {
				resourcesHolder.releaseVehicle(c.getVehicle());
			}
		});
	}

}
