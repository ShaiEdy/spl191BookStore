package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

//Events that are being handled by this MicroService:
//checkAvailabilityEvent - Future holds price.

public class InventoryService extends MicroService {
	private Inventory inventory;

	public InventoryService() {
		super("Inventory Service");
		inventory = Inventory.getInstance();
	}

	protected void initialize() {
		subscribeEvent(CheckAvailabilityEvent.class, c -> {
			synchronized (inventory) {
				int price = inventory.checkAvailabiltyAndGetPrice(c.getBookTitle());
				complete(c, price);
			}
		});
	}

}
