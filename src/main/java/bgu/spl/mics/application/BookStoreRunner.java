package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {
        Inventory inventory= Inventory.getInstance();
        BookInventoryInfo book1 = new BookInventoryInfo("Harry poter", 10, 90);
        BookInventoryInfo book2 = new BookInventoryInfo("The Hunger Games", 90, 102);
        BookInventoryInfo [] booksArray= {book1,book2};
        inventory.load(booksArray);
        inventory.printInventoryToFile("shai&Adi.txt");

        JsonParser jsonParser = new JsonParser();
        InputStream inputStream = BookStoreRunner.class.getClassLoader().getResourceAsStream("input.json");
        Reader reader = new InputStreamReader(inputStream);
        JsonElement element = jsonParser.parse(reader);
        JsonObject jsonObject = element.getAsJsonObject();
        //JsonObject booksArray = jsonObject.getAsJsonObject("initialInventory");

    }
}
