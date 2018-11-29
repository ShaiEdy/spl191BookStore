package bgu.spl.mics;

import bgu.spl.mics.application.services.APIService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Vector;

import static org.junit.Assert.*;

public class MessageBusTest {
    private MessageBus messageBus;
    private APIService APIService1;
    private Thread thread;
    private ExampleEvent exampleEvent;
    private ExampleBroadcast exampleBroadcast;


    @Before
    public void setUp() throws Exception {
        messageBus = MessageBusImpl.getMessageBus();
        APIService1 = new APIService(); // creating a new APIService
        thread = new Thread(APIService1);
        thread.start();   /// will operate run: initialize and register and subscribes
        exampleEvent = new ExampleEvent("APIService1");
        exampleBroadcast= new ExampleBroadcast("APIService1");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void subscribeEvent() throws Exception {//we subscribe a microsService to event, and than send this event and check if the future is done.
        synchronized (APIService1){
            while (! APIService1.hasBeenInitialized()) APIService1.wait();
        }
        Future future = APIService1.sendEvent(exampleEvent);
        synchronized (future) {
            while (!future.isDone()) future.wait();
        }
        assertTrue(future.isDone());
    }

    @Test
    public void subscribeBroadcast() throws Exception {

    }

    @Test
    public void complete() throws Exception {
        Future future = APIService1.sendEvent(exampleEvent);
        future.wait();
        assertTrue(future.isDone());
    }

    @Test
    public void sendBroadcast() throws Exception {

    }

    @Test
    public void sendEvent() throws Exception {

    }

    @Test
    public void register() throws Exception {
        MicroService APIService1 = new APIService(); // creating a new APIService
        Thread thread = new Thread(APIService1);
        thread.start();

        messageBus.register(APIService1); // we want to register the APIService

        //APIService1.initialize();
        ExampleBroadcast exampleBroadcast = new ExampleBroadcast("ExampleBroadcast");
        Thread.sleep(3000);
        APIService1.sendBroadcast(exampleBroadcast);


        //while (Thread.activeCount()>1){

        //}
        //ExampleEvent exampleEvent = new ExampleEvent("exampleEvent"); // Creating new event.
        //messageBus.subscribeEvent(exampleEvent.getClass(), APIService1); // we want the service to subscribe to the event
        //messageBus.sendEvent(exampleEvent); //API subscribed to exampleEvent and therfore have to get this massage
        //messageBus.complete(exampleEvent, "Finish");
    }

    @Test
    public void unregister() throws Exception {

    }

    @Test
    public void awaitMessage() throws Exception {

    }

}