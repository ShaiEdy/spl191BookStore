package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

/**
 * Passive data-object representing a receipt that should 
 * be sent to a customer after the completion of a BookOrderEvent.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class OrderReceipt implements Serializable {

	private int orderId;
	private String sellingServiceName;
	private int customerId;
	private String bookTitle;
	private int price;
	private int issuedTick;
	private int orderTick;
	private int processTick;


	public OrderReceipt(int orderId, String sellingServiceName, int customerId, String bookTitle, int price) {
		this.orderId = orderId;
		this.sellingServiceName = sellingServiceName;
		this.customerId = customerId;
		this.bookTitle = bookTitle;
		this.price = price;
	}

	/**
     * Retrieves the orderId of this receipt.
     */
	public int getOrderId() {
		return orderId;
	}

	/**
     * Retrieves the name of the selling service which handled the order.
     */
	public String getSeller() {
		return sellingServiceName;
	}
	
	/**
     * Retrieves the ID of the customer to which this receipt is issued to.
     * <p>
     * @return the ID of the customer
     */
	public int getCustomerId() {
		return customerId;
	}
	
	/**
     * Retrieves the name of the book which was bought.
     */
	public String getBookTitle() {
		return bookTitle;
	}
	
	/**
     * Retrieves the price the customer paid for the book.
     */
	public int getPrice() {
		return price;
	}
	
	/**
     * Retrieves the tick in which this receipt was issued.
     */
	public int getIssuedTick() {
		return issuedTick;
	}
	
	/**
     * Retrieves the tick in which the customer sent the purchase request.
     */
	public int getOrderTick() {
		return orderTick;
	}
	
	/**
     * Retrieves the tick in which the treating selling service started 
     * processing the order.
     */
	public int getProcessTick() {
		return processTick;
	}
}
