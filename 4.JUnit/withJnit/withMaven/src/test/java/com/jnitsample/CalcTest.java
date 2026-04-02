package com.jnitsample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CalcTest {
    @Test
    public void test() {
        Calc c = new Calc();
        int result = c.divide(10, 2);
        int expected = 5;
        assertEquals(expected, result);
    }

    @Test
    void testString(){
        String str="Junit";
        assertTrue(str.equals("Junit"));
    }
}
