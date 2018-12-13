package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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

	private Customer customer;
	private ConcurrentHashMap<Integer,Vector<String>> orderSchedule;  // as read in the input  orderSchedule

	public APIService(String name,Customer customer,ConcurrentHashMap<Integer,Vector<String>> orderSchedule) {
		super(name);
		this.customer = customer;
		this.orderSchedule = orderSchedule;
	}

	protected void initialize() {
		this.subscribeBroadcast(TickBroadcast.class, c -> {
			if (c.getTickNumber() == c.getTickDuration()) terminate();
			else if(orderSchedule.containsKey(c.getTickNumber())){
				//if the current time tick is a tick  that has saved in the orderSchedule, make buy function for those books
				for (String bookName: orderSchedule.get(c.getTickNumber())) //buy each book in the vector
				buy(bookName);
			}
		});
	}

	private void buy (String bookTitle){  // this method will be call from the main?
		BookOrderEvent bookOrderEvent= new BookOrderEvent("APIService", bookTitle, customer);
		Future<OrderReceipt> orderReceiptFuture= sendEvent(bookOrderEvent);
		OrderReceipt orderReceipt= orderReceiptFuture.get(100, TimeUnit.MILLISECONDS);
		if (orderReceipt!=null) customer.addReciept(orderReceipt);
	}
}

