package bgu.spl.mics.application;

import bgu.spl.mics.JsonReader;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

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
        //JsonObject books = jsonObject.getAsJsonArray()
        JsonArray booksArray = jsonObject.getAsJsonArray("initialInventory");

        int x=5;
    }
}
