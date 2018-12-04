package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;

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
        inventory.printInventoryToFile("shai&Adi.txt");
    }
}
