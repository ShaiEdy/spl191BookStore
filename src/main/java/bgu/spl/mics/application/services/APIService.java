package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Customer;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class APIService extends MicroService {
	private boolean initialized; //todo- maybe delete
	private Customer customer;
	private ConcurrentHashMap<Integer,Vector<String>> orderSchedule;  // as read in the input  orderSchedule
	public APIService(Customer customer) {
		super("APIService");
		this.customer=customer;
		orderSchedule= new ConcurrentHashMap<>();
	}

	@Override
	protected void initialize() {
		this.subscribeBroadcast(TickBroadcast.class, c -> {
			if (c.getTickNumber() == c.getTickDuration()) terminate();
			else if(orderSchedule.containsKey(c.getTickNumber())){
				//if the current time tick is a tick  that has saved in the orderSchedule, make buy function for those books
				for (String bookName: orderSchedule.get(c.getTickNumber())) //bu each book in the vector
				buy(bookName);
			}
		});
		initialized=true;
	}
	public boolean hasBeenInitialized(){
		return initialized;
	}
	private void buy (String bookTitle){  // this method will be call from the main?
		BookOrderEvent bookOrderEvent= new BookOrderEvent("APIService", bookTitle, customer);
		sendEvent(bookOrderEvent);
	}
}

