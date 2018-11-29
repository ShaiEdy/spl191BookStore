package bgu.spl.mics.application.passiveObjects;

import java.util.List;
import java.util.Vector;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer {
	private String name;
	private int ID;
	private String address;
	private int distance;
	private Vector<OrderReceipt> orderReceipts;
	private int availableCreditAmount;
	private int creditNumber;

	public Customer(String name, int ID, String address, int distance, int availableCreditAmount, int creditNumber ){
		this.name=name;
		this.ID=ID;
		this.address= address;
		this.distance= distance;
		this.creditNumber= creditNumber;
		this.availableCreditAmount= availableCreditAmount;
		orderReceipts= new Vector<>();
	}
	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		return name;
	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		return ID;
	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		return address;
	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		return distance;
	}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() {
		return orderReceipts;
	}
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() {
		return availableCreditAmount;
	}
	
	/**
     * Retrieves this customers credit card serial number.    
     */
	public int getCreditNumber() {
		return creditNumber;
	}
	
}
