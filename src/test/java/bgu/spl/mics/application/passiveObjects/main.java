package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.APIService;

public class main {
    public static void main(String args[]) {
        System.out.println("hello-world");
        MicroService m1= new APIService();
        MicroService m2= new APIService();
        Thread T1= new Thread(m1);
        Thread T2= new Thread(m2);
        T1.start();
        T2.start();

    }
}
