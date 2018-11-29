package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
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
	private boolean initialized;
	public APIService() {
		super("Change_This_Name");
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		this.subscribeBroadcast(ExampleBroadcast.class, c -> {
			System.out.println("BroadCast-Callback");
			synchronized (c) {
				c.notifyAll();}
		});
		this.subscribeEvent(ExampleEvent.class, c -> {
			System.out.println("Im princess Adi");
			complete(c,c.getSenderName());
		});
		initialized=true;
		synchronized (this) {
			this.notifyAll();
		}
	}
	public boolean hasBeenInitialized(){
		return initialized;
	}
}
