package bgu.spl.mics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shai on 22/11/2018.
 */
public class TempClassTest {
    TempClass i;
    @Before
    public void setUp() throws Exception {
        i = new TempClass();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void square() throws Exception {
        int output = i.square(5);
        assertEquals(25,output);
    }

}