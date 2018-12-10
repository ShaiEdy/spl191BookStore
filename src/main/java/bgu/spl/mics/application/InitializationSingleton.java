package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.concurrent.atomic.AtomicInteger;

public class InitializationSingleton {
    private AtomicInteger initializedCounter;
    private int numOfServices;
    boolean allInitialize;

    private static class singletonHolder {

        private static InitializationSingleton instance = new InitializationSingleton();
    }

    private static InitializationSingleton initializationSingleton = null; // Singleton.

    private InitializationSingleton() {
       this.numOfServices= 0;
       initializedCounter=new AtomicInteger(0);
       allInitialize= false;
    }

    public static InitializationSingleton getInstance() {
        return InitializationSingleton.singletonHolder.instance;
    }

    public void setNumOfServices(int numOfServices){
        this.numOfServices= numOfServices;
    }


    public void serviceInitialized() {
        initializedCounter.incrementAndGet();
        if (initializedCounter.get()== numOfServices){
            this.allInitialize= true;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }


    public synchronized void isAllinitialize(){
        while (!allInitialize){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
