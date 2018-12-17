package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a information about a certain book in the inventory.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class BookInventoryInfo {
	private String name;
	private AtomicInteger amount;
	private int price;

	public BookInventoryInfo(String name, int amount, int price) {
		this.name= name;
		this.amount= new AtomicInteger(amount);
		this.price=price;
	}

	public void setAmountInInventory(int newAmount){
		amount.set(newAmount);
	}

	/**
     * Retrieves the title of this book.
     * <p>
     * @return The title of this book.   
     */
	public String getBookTitle() {
		return name;
	}

	/**
     * Retrieves the amount of books of this type in the inventory.
     * <p>
     * @return amount of available books.      
     */
	public int getAmountInInventory() {
		return amount.get();
	}

	/**
     * Retrieves the price for  book.
     * <p>
     * @return the price of the book.
     */
	public int getPrice() {
		return price;
	}
	/**
	 * takes book if available (amount>0)
	 * @return true if the book could be taken and false if not
	 */
	public boolean tryToTake() {
		int oldAmount = amount.get();
		if (amount.get() > 0) {
			while (!amount.compareAndSet(oldAmount, oldAmount - 1)) {
				oldAmount = amount.get();
				if (oldAmount == 0) return false;
			}
		}
		else return false;
		return true;
	}
	
}
