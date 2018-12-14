package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.messages.TakeBookEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;

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
//TakeBookEvent - Future holds OrderResult.

public class InventoryService extends MicroService {

	private Inventory inventory;

	public InventoryService(String name) {
		super(name);
		inventory = Inventory.getInstance();
	}
	@SuppressWarnings("unchecked")
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, c -> {
			if (c.getTickNumber() == c.getTickDuration())
				terminate();
		});

		subscribeEvent(CheckAvailabilityEvent.class, c -> {
			int price = inventory.checkAvailabiltyAndGetPrice(c.getBookTitle());
			complete(c, price);
		});

		subscribeEvent(TakeBookEvent.class, c -> {
			synchronized (inventory) {
				OrderResult orderResult = inventory.take(c.getBookTitle());
				complete(c, orderResult);
			}
		});
	}
}
