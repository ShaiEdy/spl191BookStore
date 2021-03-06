package bgu.spl.mics;


import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private static class singletonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServiceToQueue; // hashMap of microServices and the related Queue
	private ConcurrentHashMap<Class, LinkedBlockingQueue<MicroService>> eventToMicroService; // hashMap of event and the subscribed micro
	private ConcurrentHashMap<Class, Vector<MicroService>> broadCastToMicroService; // hashMap of broadCast and the subscribed micro
	private ConcurrentHashMap<Message, Future> messageFutureHashMap; // hashMap of a message and the relative future object


	private MessageBusImpl() { // Constructor
		microServiceToQueue = new ConcurrentHashMap<>();
		eventToMicroService = new ConcurrentHashMap<>();
		broadCastToMicroService = new ConcurrentHashMap<>();
		messageFutureHashMap = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance() {
		return singletonHolder.instance;
	}

	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (!eventToMicroService.containsKey(type))
			eventToMicroService.put(type, new LinkedBlockingQueue<>());
		eventToMicroService.get(type).add(m);
	}

	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (broadCastToMicroService) {
			if (!broadCastToMicroService.containsKey(type))
				broadCastToMicroService.put(type, new Vector<>());
		}
		broadCastToMicroService.get(type).add(m);
	}
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) { // microService call this when it finish the callback
		Future future = messageFutureHashMap.get(e);
		future.resolve(result); //set the future that the send event microService got as a promise to result
	}

	public void sendBroadcast(Broadcast b) {
		if (!broadCastToMicroService.containsKey(b.getClass())) return; //if nobody ever registered to this broadCast return null.
		Vector<MicroService> microServiceVector = broadCastToMicroService.get(b.getClass());
		Iterator microServiceVectorIterator = microServiceVector.iterator();
		synchronized (microServiceVector) { //synchronizing it to prevent removing microServices from the vector while iterating.
			while (microServiceVectorIterator.hasNext()) {
				MicroService microService = (MicroService) microServiceVectorIterator.next();
				microServiceToQueue.get(microService).add(b);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public <T> Future<T> sendEvent(Event<T> e) {
		Future future = new Future();
		messageFutureHashMap.put(e, future);
		if (!eventToMicroService.containsKey(e.getClass())) {
			complete(e,null);
			return future;// if no microService subscribed to this event return null
		}
		MicroService microService = null;
		LinkedBlockingQueue<MicroService> microServiceQueueByEvent = eventToMicroService.get(e.getClass());
		synchronized (microServiceQueueByEvent) {
			microService = microServiceQueueByEvent.poll();
			if (microService == null) {
				complete(e,null);
				return future; // if microService=null it means that all the microService that have subscribed to this event have been unregistered.
			}
			eventToMicroService.get(e.getClass()).add(microService);  // return the microService to the queue of the event
			LinkedBlockingQueue messageQueueOfMicroService =  microServiceToQueue.get(microService);
			synchronized (messageQueueOfMicroService) {
				messageQueueOfMicroService.add(e); // we don't want nobody to touch the queue while we add to it.
			}
		}
		return future;
	}

	public void register(MicroService m) {
		microServiceToQueue.put(m, new LinkedBlockingQueue<>()); //add new place in hashMap for m and his queue
	}
	@SuppressWarnings("unchecked")
	public void unregister(MicroService m) { //
		if (microServiceToQueue.get(m) != null) { //if was registered

			Iterator<Class> eventSetIter = eventToMicroService.keySet().iterator(); // we iterate through all the keys and remove m where it found
			while (eventSetIter.hasNext()) {
				Class c = eventSetIter.next();
				LinkedBlockingQueue<MicroService> microServiceQueueByEvent = eventToMicroService.get(c);
				synchronized (microServiceQueueByEvent) {
					microServiceQueueByEvent.remove(m); // We don't want to remove m from the queue if event is being sent in the same time.
				}
			}

			Iterator<Class> BroadCastSetIter = broadCastToMicroService.keySet().iterator(); // we iterate through all the keys and remove m where it found
			while (BroadCastSetIter.hasNext()) {
				Class c = BroadCastSetIter.next();
				Vector<MicroService> microServiceVector = broadCastToMicroService.get(c); //vector of the microServices subscribed to c
				synchronized (microServiceVector) { // We don't want to remove a microService while its being iterated by sendBroadcast
					microServiceVector.remove(m); //remove m if exist
				}
			}
			LinkedBlockingQueue messageQueueOfMicroService = microServiceToQueue.get(m);
			synchronized (messageQueueOfMicroService) { // we don't want nobody to touch the queue while we remove it.
				for (Object o : messageQueueOfMicroService) { // we iterate through all the messages that were left in the queue and putting null in their future elements.
					Message message = (Message) o;
					if (message instanceof Event)
						complete((Event) message, null);
				}
				microServiceToQueue.remove(m); // we remove the microService itself.
			}
		}
	}

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