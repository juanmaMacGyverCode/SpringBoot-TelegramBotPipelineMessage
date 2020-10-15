package com.juanma.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    private CalculatorService calculatorService = new CalculatorService();

    @Test
    public void testSum() {
        assertEquals(6, calculatorService.sum(2,3));
    }
}
