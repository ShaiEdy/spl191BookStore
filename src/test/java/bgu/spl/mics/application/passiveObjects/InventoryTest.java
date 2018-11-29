package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * Created by Shai on 22/11/2018.
 */
public class InventoryTest {
    Inventory inventory;
    BookInventoryInfo harryPotterBook;
    BookInventoryInfo AliceInTheWonderland;
    BookInventoryInfo[] bookInventoryInfoHarry;
    BookInventoryInfo[] bookInventoryInfoAlice;


    @Before
    public void setUp() throws Exception {
        Field instance = Inventory.class.getDeclaredField("inventory");
        instance.setAccessible(true);
        instance.set(null,null);
        inventory = Inventory.getInstance();
        harryPotterBook = new BookInventoryInfo("HARRY-POTTER", 1, 100);
        bookInventoryInfoHarry = new BookInventoryInfo[1];
        bookInventoryInfoHarry[0] = harryPotterBook;
        AliceInTheWonderland = new BookInventoryInfo("AliceInTheWonderland", 0, 200);
        bookInventoryInfoAlice = new BookInventoryInfo[1];
        bookInventoryInfoAlice[0] = AliceInTheWonderland;
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getInstance() throws Exception { // we want to check if it make new inventory only when is not exist yet.
        Inventory inventory2 = Inventory.getInstance();
        assertEquals(inventory2, inventory);
    }

    @Test
    public void load() throws Exception {

        int price;
        price = inventory.checkAvailabiltyAndGetPrice("HARRY-POTTER");
        assertEquals(-1, price);
        inventory.load(bookInventoryInfoHarry);
        price = inventory.checkAvailabiltyAndGetPrice("HARRY-POTTER");
        assertEquals(price, 100);
        inventory.load(bookInventoryInfoAlice);
        price = inventory.checkAvailabiltyAndGetPrice("AliceInTheWonderland");
        assertEquals(-1, price);
    }

    @Test
    public void take() throws Exception {
        assertEquals(OrderResult.NOT_IN_STOCK, inventory.take("HARRY-POTTER")); ///???
        inventory.load(bookInventoryInfoHarry);
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, inventory.take("HARRY-POTTER"));
        assertEquals(OrderResult.NOT_IN_STOCK, inventory.take("HARRY-POTTER")); /// this time no Harry anymore (was only 1)
        assertEquals(OrderResult.NOT_IN_STOCK, inventory.take("AliceInTheWonderland")); ///alice amount is 0
    }

    @Test
    public void checkAvailabiltyAndGetPrice() throws Exception {
        int price;
        price = inventory.checkAvailabiltyAndGetPrice("HARRY-POTTER");
        assertEquals(-1, price);
        inventory.load(bookInventoryInfoHarry);
        price = inventory.checkAvailabiltyAndGetPrice("HARRY-POTTER");
        assertEquals(price, 100);
        inventory.load(bookInventoryInfoAlice);
        price = inventory.checkAvailabiltyAndGetPrice("AliceInTheWonderland");
        assertEquals(-1, price);
    }

    @Test
    public void printInventoryToFile() throws Exception { }
}