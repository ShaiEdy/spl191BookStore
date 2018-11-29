package bgu.spl.mics;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.APIService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

public class main {
    public static void main(String args[]) {
        System.out.println("hello-world");
        MicroService m1= new APIService();
        MicroService m2= new APIService();
        Thread T1= new Thread(m1);
        Thread T2= new Thread(m2);
        T1.start();
        T2.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExampleEvent exampleEvent = new ExampleEvent("m1");
        ExampleBroadcast exampleBroadcast = new ExampleBroadcast("m1");
        Future future = m1.sendEvent(exampleEvent);
        m1.sendBroadcast(exampleBroadcast);
        System.out.println(future.isDone());
        MessageBus messageBus = MessageBusImpl.getMessageBus();

    }
}
