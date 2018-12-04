package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TakeBookEvent;
import bgu.spl.mics.application.passiveObjects.*;

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
		subscribeEvent(BookOrderEvent.class, c -> {
			CheckAvailabilityEvent checkAvailabilityEvent = new CheckAvailabilityEvent(getName(), c.getBookTitle());
			Future<Integer> checkAvailabilityEventFuture = sendEvent(checkAvailabilityEvent);
			Integer price = checkAvailabilityEventFuture.get();
			if (price == -1) complete(c, null); // if book doesnt exist or not in stock
			else {
				Customer customer = c.getCustomer();
				int amount = customer.getAvailableCreditAmount();
				if (price > amount)  // if Customer doesn't have enough money.
					complete(c, null);
				else { // we need to process the purchase
					TakeBookEvent takeBookEvent = new TakeBookEvent(getName(), c.getBookTitle());
					Future<OrderResult> takeBookEventFuture = sendEvent(takeBookEvent);
					OrderResult orderResult = takeBookEventFuture.get();
					if (orderResult == OrderResult.SUCCESSFULLY_TAKEN) {
						moneyRegister.chargeCreditCard(customer, amount); // function setAvailableCreditAmount in customer is synchronized
						OrderReceipt orderReceipt = new OrderReceipt(0, getName(), c.getCustomer().getId(), c.getBookTitle(), price);
						moneyRegister.file(orderReceipt);
						complete(c, orderReceipt);
						DeliveryEvent deliveryEvent = new DeliveryEvent(customer.getAddress(), customer.getDistance());
						sendEvent(deliveryEvent);
					}
				}
			}
		});
	}
}
