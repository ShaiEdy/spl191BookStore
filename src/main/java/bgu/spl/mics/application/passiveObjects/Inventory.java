package bgu.spl.mics.application.passiveObjects;


import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private static class singletonHolder {
		private static Inventory instance = new Inventory();
	}

	private static Inventory inventory = null; // Singleton.
	private ConcurrentHashMap<String,BookInventoryInfo> bookInventoryInfo;

	private Inventory() {
		bookInventoryInfo= new ConcurrentHashMap<>();
		// the books will be loaded in load function
	}
	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		return singletonHolder.instance;
	}
	
	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (BookInventoryInfo[ ] inventory ) {
		for (BookInventoryInfo book : inventory) {
			bookInventoryInfo.put(book.getBookTitle(), book);
		}

	}
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
     */
	public OrderResult take (String book) { ///only customer with money gets here
		BookInventoryInfo bookInfo = bookInventoryInfo.get(book);
		if (bookInfo != null) {
			int amount = bookInfo.getAmountInInventory();
			if (amount != 0 && bookInfo.tryToTake()) {
				return OrderResult.SUCCESSFULLY_TAKEN;
			}
		}
		return OrderResult.NOT_IN_STOCK;
	}

	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
	public int checkAvailabiltyAndGetPrice(String book) {
		BookInventoryInfo bookInfo = bookInventoryInfo.get(book);
		int price = -1;
		if (bookInfo != null) {
			if (bookInfo.getAmountInInventory() != 0) price = bookInfo.getPrice();
		}
		return price;
	}

	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
     */
	public void printInventoryToFile(String filename){
		HashMap<String,Integer> inventoryHashMap= new HashMap<>(); // this hashMap will be printed
		Iterator keySetIterator= bookInventoryInfo.keySet().iterator();
		while (keySetIterator.hasNext()){
			String bookName= (String)keySetIterator.next();
			Integer amount = bookInventoryInfo.get(bookName).getAmountInInventory();
			inventoryHashMap.put(bookName, amount); //put in the hashMap that will be printed only the name and the amount
		}
		try{
			FileOutputStream fileOut= new FileOutputStream((filename));
			ObjectOutputStream  out = new ObjectOutputStream(fileOut);
			out.writeObject(inventoryHashMap);

			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
