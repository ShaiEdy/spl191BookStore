package bgu.spl.mics.application;

import bgu.spl.mics.JsonReader;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
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
        Inventory inventory= Inventory.getInstance();
        BookInventoryInfo book1 = new BookInventoryInfo("Harry poter", 10, 90);
        BookInventoryInfo book2 = new BookInventoryInfo("The Hunger Games", 90, 102);
        //BookInventoryInfo [] booksArray= {book1,book2};
        //inventory.load(booksArray);
        //inventory.printInventoryToFile("shai&Adi.txt");


        JsonReader jsonReader = new JsonReader();
        JsonObject jsonObject = jsonReader.readFile();

        // --------- Read the books from input.json-------------
        JsonArray booksArray = jsonObject.getAsJsonArray("initialInventory"); // books array is jsonObject representing the books.
        Iterator booksIterator = booksArray.iterator(); // we iterate through all the books
        BookInventoryInfo[] bookInventoryInfos = new BookInventoryInfo[booksArray.size()]; // we open array that will be later loaded to inventory
        int counter = 0; // represents the place in the array to insert the book to
        while (booksIterator.hasNext()){
            JsonObject bookInfo = (JsonObject) booksIterator.next();
            String bookTitle = bookInfo.get("bookTitle").getAsString();
            int bookAmount = bookInfo.get("amount").getAsInt();
            int bookPrice = bookInfo.get("price").getAsInt();
            BookInventoryInfo bookInventoryInfo = new BookInventoryInfo(bookTitle,bookAmount,bookPrice);
            bookInventoryInfos[counter] = bookInventoryInfo;
            counter++;
        }

        // --------- Read the vehicles from input.json-------------
        JsonArray vehiclesArray = jsonObject.getAsJsonArray("initialResources"); // vehicles array is jsonObject representing the vehicles.
        Iterator vehiclesIterator = vehiclesArray.iterator(); // we iterate through all the vehicles
        BookInventoryInfo[] bookInventoryInfos = new BookInventoryInfo[booksArray.size()]; // we open array that will be later loaded to inventory
        int counter = 0; // represents the place in the array to insert the book to
        while (booksIterator.hasNext()){
            JsonObject bookInfo = (JsonObject) booksIterator.next();
            String bookTitle = bookInfo.get("bookTitle").getAsString();
            int bookAmount = bookInfo.get("amount").getAsInt();
            int bookPrice = bookInfo.get("price").getAsInt();
            BookInventoryInfo bookInventoryInfo = new BookInventoryInfo(bookTitle,bookAmount,bookPrice);
            bookInventoryInfos[counter] = bookInventoryInfo;
            counter++;
        }


        int x=5;

    }
}
