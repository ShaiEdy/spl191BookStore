package bgu.spl.mics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by Shai on 22/11/2018.
 */
public class FutureTest {
    Future<String> future;
    @Before
    public void setUp() throws Exception {
        future = new Future<String>();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void get() throws Exception {  //we want to check the the returned object is the correct one
        String result= "Shai";
        //// how can we test that when is not dine, its wait forever?
        future.resolve(result);
        assertEquals("Shai",future.get());
    }

    @Test
    public void resolve() throws Exception {
        String result= "Shai";
        TimeUnit timeUnit=TimeUnit.MILLISECONDS;
        assertEquals(null,future.get(10,timeUnit));
        future.resolve(result);
        assertEquals(result,future.get());
    }

    @Test
    public void isDone() throws Exception {
        assertFalse(future.isDone());
        future.resolve("Adi");
        assertTrue(future.isDone());
    }

    @Test
    public void get1() throws Exception {
        String result= "Adi";
        TimeUnit timeUnit=TimeUnit.MILLISECONDS;
        assertEquals(null,future.get(10,timeUnit));
        future.resolve(result);
        assertEquals("Adi",future.get(10,timeUnit));

    }

}