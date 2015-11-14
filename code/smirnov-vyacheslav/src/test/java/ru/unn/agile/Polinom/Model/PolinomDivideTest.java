package test.java.ru.unn.agile.Polinom.Model;

import org.junit.Assert; 
import org.junit.Test;
import org.junit.Before; 

import java.util.Arrays;

import main.java.ru.unn.agile.Polinom.Model.*;

public class PolinomDivideTest {
    private Polinom first;
    private Polinom second;

    @Before
    public void initPolinoms() {
        first = new Polinom(new double[]{8.0, 2.0, 0.0, 1.0});
        second = new Polinom(new double[]{10.0, 0.0, -6.0, 5.0, -2.0});
    }

    @Test(expected = Exception.class)
    public void canNotDivideByZero() {
        first.divide(new Polinom());
    }

    @Test(expected = Exception.class)
    public void canNotDivideByLargeDegree() {
        first.divide(second);
    }

    @Test
    public void canDivideByOne() {
        Polinom one = new Polinom(new double[]{1.0});

        first.divide(one);

        Assert.assertTrue(Arrays.equals(first.getCoefficients(), new double[]{8.0, 2.0, 0.0, 1.0}));
    }

    @Test
    public void canDivideBySimplePolinom() {
        Polinom simplePolinom = new Polinom(new double[]{0.0, 5.0});

        first.divide(simplePolinom);

        Assert.assertTrue(Arrays.equals(first.getCoefficients(), new double[]{0.4, 0.0, 0.2}));
    }

    @Test
    public void canDividePolinoms() {
        second.divide(first);

        Assert.assertTrue(Arrays.equals(first.getCoefficients(), new double[]{5.0, -2.0}));
    }
}
