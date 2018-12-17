package bgu.spl.mics.application.passiveObjects;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister implements Serializable {
	private Vector<OrderReceipt> orderReceipts;
	//private int totalEarning;
	private AtomicInteger totalEarning;
	private static class singletonHolder {
		private static MoneyRegister instance = new MoneyRegister();
	}


	private MoneyRegister() {
		totalEarning = new AtomicInteger(0);
		orderReceipts = new Vector<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MoneyRegister getInstance() {
		return singletonHolder.instance;
	}

	/**
	 * Saves an order receipt in the money register.
	 * <p>
	 * @param r     The receipt to save in the money register.
	 */
	public void file (OrderReceipt r) {
		orderReceipts.add(r);
		int totalEarningTemp = totalEarning.get(); // preparing for the atomicInteger.
		while(!totalEarning.compareAndSet(totalEarningTemp,totalEarningTemp+r.getPrice())){
			totalEarningTemp = totalEarning.get();
		}
	}

	/**
	 * Retrieves the current total earnings of the store.
	 */
	public int getTotalEarnings() {
		return totalEarning.get();
	}

	/**
	 * Charges the credit card of the customer a certain amount of money.
	 * <p>
	 * @param amount    amount to charge
	 */
	public void chargeCreditCard(Customer c, int amount) { //assume the customer has enough money
		c.setAvailableCreditAmount(amount);
	}

	/**
	 * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts
	 * currently in the MoneyRegister
	 * This method is called by the main method in order to generate the output..
	 */
	public void printOrderReceipts(String filename) { //using serialized of java
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(orderReceipts);
			out.close();
			fileOut.close();

		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
