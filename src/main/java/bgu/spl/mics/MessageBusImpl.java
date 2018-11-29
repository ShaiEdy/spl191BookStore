package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

import javax.jnlp.UnavailableServiceException;
import javax.management.openmbean.OpenMBeanConstructorInfo;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServiceToQueue; // hashMap of microServices and the related Queue
	private ConcurrentHashMap<Class, LinkedBlockingQueue<MicroService>> eventToMicroService; // hashMap of event and the subscribed micro
	//todo: maybe replacing the vector with arrayList
	private ConcurrentHashMap<Class, Vector<MicroService>> broadCastToMicroService; // hashMap of broadCast and the subscribed micro
	private ConcurrentHashMap<Message, Future> messageFutureHashMap; // hashMap of a message and the relative future object

	private static MessageBusImpl messageBus = null; // Singleton.

	private MessageBusImpl() { // Constructor
		microServiceToQueue = new ConcurrentHashMap<>();
		eventToMicroService = new ConcurrentHashMap<>(); //TODO: make sure we add all the events as keys to this hashmap
		eventToMicroService.put(ExampleEvent.class, new LinkedBlockingQueue<>());
		broadCastToMicroService = new ConcurrentHashMap<>(); //TODO: make sure we add all the broadCasts as keys to this hashmap
		broadCastToMicroService.put(ExampleBroadcast.class, new Vector<>());
		messageFutureHashMap = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getMessageBus() { //todo
		if (messageBus == null)
			messageBus = new MessageBusImpl();
		return messageBus;
	}

	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		eventToMicroService.get(type).add(m);
	}

	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		broadCastToMicroService.get(type).add(m);
	}

	public <T> void complete(Event<T> e, T result) { // microService call this when it finish the callback
		Future future = messageFutureHashMap.get(e);
		future.resolve(result);
	}

	public void sendBroadcast(Broadcast b) {
		Vector<MicroService> microServiceVector = broadCastToMicroService.get(b.getClass());
		Iterator microServiceVectorIterator = microServiceVector.iterator();
		while (microServiceVectorIterator.hasNext()) {
			MicroService microService = (MicroService) microServiceVectorIterator.next();
			microServiceToQueue.get(microService).add(b);
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future future = new Future();
		messageFutureHashMap.put(e, future);
		MicroService microService = null;
		try {
			microService = eventToMicroService.get(e.getClass()).take();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		microServiceToQueue.get(microService).add(e);
		eventToMicroService.get(e.getClass()).add(microService);  // return the microService to the queue of the event
		return future;
	}

	public void register(MicroService m) {
		microServiceToQueue.put(m, new LinkedBlockingQueue<>()); //add new place in hashMap for m and his queue
	}

	@Override
	public void unregister(MicroService m) {
		if (microServiceToQueue.get(m) != null) { //if was registered
			microServiceToQueue.remove(m);
			Iterator<Class> eventSetIter = eventToMicroService.keySet().iterator(); // we iterate through all the keys and remove m where it found
			while (eventSetIter.hasNext()) {
				Class c = eventSetIter.next();
				eventToMicroService.get(c).remove(m);
			}
			Iterator<Class> BroadCastSetIter = broadCastToMicroService.keySet().iterator(); // we iterate through all the keys and remove m where it found
			while (BroadCastSetIter.hasNext()) {
				Class c = BroadCastSetIter.next();
				broadCastToMicroService.get(c).remove(m);
			}
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		try {
			if (microServiceToQueue.containsKey(m)) { // If TRUE, then m was registered.
				return microServiceToQueue.get(m).take();// the blockingQueue assure that there is something in the queue or it will wait
			} else {
				throw new IllegalStateException( "Micro-Service was not registered.");
			}
		} catch (InterruptedException e1) {
			throw new InterruptedException("");
		}
	}
}