package org.academy.maven;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */

    private static final double DELTA = 1e-1;

    @Test
    public void shouldAddTrue()
    {
        assertEquals(8.0, App.addition(4,4),DELTA);
    }

    @Test
    public void shouldMinusTrue()
    {
        assertEquals(41.0, App.minus(50,9),DELTA);
    }

    @Test
    public void shouldMultiplyTrue()
    {
        assertEquals(55.0, App.multiplication(5,11),DELTA);
    }

    @Test
    public void shouldDivideTrue()
    {
        assertEquals(2.3, App.division(7,3),DELTA);
    }
}
