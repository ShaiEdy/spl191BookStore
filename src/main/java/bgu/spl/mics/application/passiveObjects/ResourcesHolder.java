package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {
	private LinkedBlockingQueue<DeliveryVehicle> cars;
	private int free;
	private int permits;

	private static class singletonHolder {
		private static ResourcesHolder instance = new ResourcesHolder();
	}
	private ResourcesHolder() {
		cars=new LinkedBlockingQueue<>();
		free=0;
		permits=0;
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static ResourcesHolder getInstance() {
		return ResourcesHolder.singletonHolder.instance;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		Future future = new Future();
		try {
			DeliveryVehicle deliveryVehicle = cars.take();
			future.resolve(deliveryVehicle);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		try {
			cars.put(vehicle);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		cars.addAll(Arrays.asList(vehicles));
	}

}
