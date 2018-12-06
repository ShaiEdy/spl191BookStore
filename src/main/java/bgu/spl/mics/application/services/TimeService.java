package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{
	private int currentTimeTick;
	private int speed;
	private int duration; // number of ticks until the program

	public TimeService(String name) {
		super(name);

	}

	public TimeService(int speed, int duration){
		super("TimeService");
		this.speed = speed;
		this.duration = duration;
		currentTimeTick=0;
	}

	@Override
	protected void initialize() {
		while (currentTimeTick<=duration){
			TickBroadcast tickBroadcast= new TickBroadcast(getName(),currentTimeTick,duration);
			sendBroadcast(tickBroadcast);
			currentTimeTick= currentTimeTick+1;
			try {
				Thread.sleep(speed); // sleep is the number of milliSec between ticks
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.terminate();
	}

}
