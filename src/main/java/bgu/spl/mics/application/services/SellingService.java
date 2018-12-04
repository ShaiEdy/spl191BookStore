package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService {
	private MoneyRegister moneyRegister;
	private int orderId = 0;

	public SellingService() {
		super("Selling Service");
		moneyRegister = MoneyRegister.getInstance();
	}

	protected void initialize() {
		subscribeEvent(BookOrderEvent.class, new Callback<BookOrderEvent>() {
			public void call(BookOrderEvent c) {
				CheckAvailabilityEvent checkAvailabilityEvent = new CheckAvailabilityEvent(getName(), c.getBookTitle());
				Future<Integer> future = sendEvent(checkAvailabilityEvent);
				Integer price = future.get();
				if (price == -1) complete(c, null);
				else {
					Customer customer = c.getCustomer();
					int amount = customer.getAvailableCreditAmount();
					if (price > amount) { // if Customer doesnt have enough money.

					}
					OrderReceipt orderReceipt = new OrderReceipt(0, getName(), c.getCustomer().getId(), c.getBookTitle(), price);
					complete(c, orderReceipt);
				}
			}
		});
	}
}
