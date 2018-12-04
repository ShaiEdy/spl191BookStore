package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

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

	public APIService() {
		super("APIService");
	}

	@Override
	protected void initialize() {
		this.subscribeBroadcast(TickBroadcast.class, c -> {
			System.out.println("tick"); //change this
		});
		this.subscribeEvent(BookOrderEvent.class, c -> complete(c,c.getSenderName()));
		initialized=true;
	}
	public boolean hasBeenInitialized(){
		return initialized;
	}
}
