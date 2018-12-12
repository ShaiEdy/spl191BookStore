package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {
        args= new String[6]; //todo delete
        args[1]="src/input.json"; args[2]= "a.txt"; args[3]= "b.txt"; args[4]="c.txt"; args[5]="d.txt";

        InitializationSingleton initializationSingleton= InitializationSingleton.getInstance(); // singleTone for counting the servers - we use it to make sure the servers dont miss the first timeTick
        int servicesCounter=0; // the counter

        //--------------------------------Parsing the json file----------------------------------------------
        JsonParser jsonParser = new JsonParser();
        File file = new File(args[1]);
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            inputStream = null;
        }

        Reader reader = new InputStreamReader(inputStream);
        JsonElement element = jsonParser.parse(reader);
        JsonObject jsonObject = element.getAsJsonObject();
        HashMap<Integer,Customer> integerCustomerHashMap= new HashMap<>(); // initialize hashMap that will be printed at the end of the program

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
        JsonObject carsObject = vehiclesArray.get(0).getAsJsonObject();
        JsonArray carsArray = carsObject.getAsJsonArray("vehicles");
        Iterator vehiclesIterator = carsArray.iterator(); // we iterate through all the vehicles
        DeliveryVehicle[] vehicles = new DeliveryVehicle[carsArray.size()]; // we open array that will be later loaded to inventory
        counter = 0; // represents the place in the array to insert the car to
        while (vehiclesIterator.hasNext()) {
            JsonObject vehicleInfo = (JsonObject) vehiclesIterator.next();
            int license = vehicleInfo.get("license").getAsInt();
            int speed = vehicleInfo.get("speed").getAsInt();
            DeliveryVehicle deliveryVehicle = new DeliveryVehicle(license, speed);
            vehicles[counter] = deliveryVehicle;
            counter++;
        }
        ResourcesHolder resourcesHolder = ResourcesHolder.getInstance();
        resourcesHolder.load(vehicles);

        // --------- Read the Services from input.json-------------
        JsonObject servicesArray = jsonObject.getAsJsonObject("services"); // vehicles array is jsonObject representing the vehicles.

        // --sellingService--
        int numberOfSellingServices = servicesArray.get("selling").getAsInt();
        for (int i = 0; i < numberOfSellingServices; i++) {
            String name = "SellingService" + i;
            SellingService sellingService = new SellingService(name);
            Thread sellingServiceThread = new Thread(sellingService, name);
            servicesCounter++;
            sellingServiceThread.start();
        }

        // --inventoryService--
        int numberOfInventoryServices = servicesArray.get("inventoryService").getAsInt();
        for (int i = 0; i < numberOfInventoryServices; i++) {
            String name = "InventoryService" + i;
            InventoryService inventoryService = new InventoryService(name);
            Thread inventoryServiceThread = new Thread(inventoryService, name);
            servicesCounter++;
            inventoryServiceThread.start();
        }

        // --logisticsService--
        int numberOfLogisticsServices = servicesArray.get("logistics").getAsInt();
        for (int i = 0; i < numberOfLogisticsServices; i++) {
            String name = "LogisticService" + i;
            LogisticsService logisticsService = new LogisticsService(name);
            Thread logisticsServiceThread = new Thread(logisticsService, name);
            servicesCounter++;
            logisticsServiceThread.start();
        }

        // --resourcesService--
        int numberOfResourcesServices = servicesArray.get("resourcesService").getAsInt();
        for (int i = 0; i < numberOfResourcesServices; i++) {
            String name = "ResourceService" + i;
            ResourceService resourceService = new ResourceService(name);
            Thread resourceServiceThread = new Thread(resourceService, name);
            servicesCounter++;
            resourceServiceThread.start();
        }

        // --------- Read the Customers (API) from input.json-------------
        JsonArray customersArray = servicesArray.get("customers").getAsJsonArray();
        Iterator customersIterator = customersArray.iterator(); // go trough all the customers
        int counterCustomers = 0;
        while (customersIterator.hasNext()) {
            JsonObject jsonCustomer = (JsonObject) customersIterator.next();
            String name = jsonCustomer.get("name").getAsString();
            int ID = jsonCustomer.get("id").getAsInt();
            String address = jsonCustomer.get("address").getAsString();
            int distance = jsonCustomer.get("distance").getAsInt();
            JsonObject creditArray = jsonCustomer.get("creditCard").getAsJsonObject();
            int creditNum = creditArray.get("number").getAsInt();
            int amount = creditArray.get("amount").getAsInt();
            Customer customer = new Customer(name, ID, address, distance, amount, creditNum);
            integerCustomerHashMap.put(ID,customer);
            String APIname = "APIService" + counterCustomers;
            APIService apiService = new APIService(APIname, customer);
            Thread apiServiceThread = new Thread(apiService, name);
            servicesCounter++;
            apiServiceThread.start();
            counterCustomers++;
        }

        // --timeService--
        TimeService timeService = new TimeService(servicesArray.getAsJsonObject("time").get("speed").getAsInt(), servicesArray.getAsJsonObject("time").get("duration").getAsInt());
        Thread timeServiceThread = new Thread(timeService, "TimeService");
        initializationSingleton.setNumOfServices(servicesCounter); // we set the number of time service that have been initialized.
        initializationSingleton.isAllinitialize(); //blocking method- wait till all the services are initialized
        timeServiceThread.start();

        ///main- wait till all the threads are dead
        // then print everything

        //print inventory
        inventory.printInventoryToFile(args[3]);

        //print customers hashMap
        try {
            FileOutputStream fileOut = new FileOutputStream(args[2]);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(integerCustomerHashMap);
            out.close();
            fileOut.close();
            System.out.println("\n Customer hashMap Serialization Successful... Checkout your specified output file..\n");

        }catch (IOException e) {
            e.printStackTrace();
        }

        //print the orderReceipt
        MoneyRegister moneyRegister= MoneyRegister.getInstance();
        moneyRegister.printOrderReceipts(args[4]);

        //print the moneyRegister
        try {
            //Saving of object in a file
            FileOutputStream fileOut = new FileOutputStream(args[5]);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            // Method for serialization of object
            out.writeObject(moneyRegister);

            out.close();
            fileOut.close();
            System.out.println("\n MoneyReg Serialization Successful... Checkout your specified output file..\n");

        }catch (IOException e) {
            e.printStackTrace();
        }


        System.exit(0); // todo:Check about AGENT_ERROR_NO_JNI_ENV(183): error.
    }
}
