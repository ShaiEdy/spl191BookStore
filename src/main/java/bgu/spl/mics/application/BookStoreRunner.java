package bgu.spl.mics.application;

import bgu.spl.mics.JsonReader;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.services.SellingService;
import bgu.spl.mics.application.services.TimeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {
        JsonReader jsonReader = new JsonReader();
        JsonObject jsonObject = jsonReader.readFile();

        // --------- Read the books from input.json-------------
        JsonArray booksArray = jsonObject.getAsJsonArray("initialInventory"); // books array is jsonObject representing the books.
        Iterator booksIterator = booksArray.iterator(); // we iterate through all the books
        BookInventoryInfo[] bookInventoryInfos = new BookInventoryInfo[booksArray.size()]; // we open array that will be later loaded to inventory
        int counter = 0; // represents the place in the array to insert the book to
        while (booksIterator.hasNext()) {
            JsonObject bookInfo = (JsonObject) booksIterator.next();
            String bookTitle = bookInfo.get("bookTitle").getAsString();
            int bookAmount = bookInfo.get("amount").getAsInt();
            int bookPrice = bookInfo.get("price").getAsInt();
            BookInventoryInfo bookInventoryInfo = new BookInventoryInfo(bookTitle, bookAmount, bookPrice);
            bookInventoryInfos[counter] = bookInventoryInfo;
            counter++;
        }
        Inventory inventory = Inventory.getInstance();
        inventory.load(bookInventoryInfos);

       // --------- Read the vehicles from input.json-------------
        JsonArray vehiclesArray = jsonObject.getAsJsonArray("initialResources"); // vehicles array is jsonObject representing the vehicles.
        JsonObject carsObject= vehiclesArray.get(0).getAsJsonObject();
        JsonArray carsArray= carsObject.getAsJsonArray("vehicles");
        Iterator vehiclesIterator = carsArray.iterator(); // we iterate through all the vehicles
        DeliveryVehicle[] vehicles= new DeliveryVehicle[carsArray.size()]; // we open array that will be later loaded to inventory
        counter= 0; // represents the place in the array to insert the car to
        while (vehiclesIterator.hasNext()){
            JsonObject vehicleInfo = (JsonObject) vehiclesIterator.next();
            int license = vehicleInfo.get("license").getAsInt();
            int speed = vehicleInfo.get("speed").getAsInt();
            DeliveryVehicle deliveryVehicle = new DeliveryVehicle(license,speed);
            vehicles[counter] = deliveryVehicle;
            counter++;
        }
        ResourcesHolder resourcesHolder= ResourcesHolder.getInstance();
        resourcesHolder.load(vehicles);

        // --------- Read the Services from input.json-------------
        JsonObject servicesArray = jsonObject.getAsJsonObject("services"); // vehicles array is jsonObject representing the vehicles.

        // --timeService--
        TimeService timeService = new TimeService(servicesArray.getAsJsonObject("time").get("speed").getAsInt(),servicesArray.getAsJsonObject("time").get("duration").getAsInt());
        Thread timeServiceThread = new Thread(timeService);
        timeServiceThread.start();

        // --sellingService--
        int numberOfSellingServices = servicesArray.get("selling").getAsInt();
        for (int i = 0; i < numberOfSellingServices; i++) {
            String name = "SellingService" + i;
            SellingService sellingService = new SellingService(name);
            Thread sellingServiceThread= new Thread(timeService);
            sellingServiceThread.start();
        }

        // --sellingService--
    }
}
