package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
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
	private int currentTick;

	public SellingService(String name) {
		super(name);
		moneyRegister = MoneyRegister.getInstance();
		currentTick = 0;
	}

	@SuppressWarnings("unchecked")
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, c -> {
			currentTick = c.getTickNumber();
			if (currentTick == c.getTickDuration())
				terminate();
		});
		subscribeEvent(BookOrderEvent.class, c -> {
			CheckAvailabilityEvent checkAvailabilityEvent = new CheckAvailabilityEvent(getName(), c.getBookTitle());
			Future<Integer> checkAvailabilityEventFuture = sendEvent(checkAvailabilityEvent);
			Integer price = checkAvailabilityEventFuture.get();
			if (price == -1) complete(c, null); // if book doesnt exist or not in stock
			else {
				Customer customer = c.getCustomer();
				synchronized (customer) {
					int amount = customer.getAvailableCreditAmount();
					if (price > amount)  // if Customer doesn't have enough money.
						complete(c, null);
					else { // we need to process the purchase
						TakeBookEvent takeBookEvent = new TakeBookEvent(getName(), c.getBookTitle());
						Future<OrderResult> takeBookEventFuture = sendEvent(takeBookEvent);
						OrderResult orderResult = takeBookEventFuture.get();
						// notice that if inventoryService have been unregistered than orderResult will be not successfully taken
						if (orderResult == OrderResult.SUCCESSFULLY_TAKEN) {
							DeliveryEvent deliveryEvent = new DeliveryEvent(customer.getAddress(), customer.getDistance());
							Future<Boolean> deliverySucceed = sendEvent(deliveryEvent);
							if (deliverySucceed.get()) { //only if the delivery succeed:
								moneyRegister.chargeCreditCard(customer, price); // function setAvailableCreditAmount in customer is synchronized
								OrderReceipt orderReceipt = new OrderReceipt(0, getName(), c.getCustomer().getId(), c.getBookTitle(), price, currentTick, c.getOrderTick());
								moneyRegister.file(orderReceipt);
								complete(c, orderReceipt);
							} else complete(c, null); // the order was'nt successful
						} else complete(c, null); // the order was'nt successful
					}
				}
			}
		});
	}
}
