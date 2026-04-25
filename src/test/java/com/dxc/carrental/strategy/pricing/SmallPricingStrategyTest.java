package com.dxc.carrental.strategy.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SmallPricingStrategyTest {
    private PricingStrategy strategy;

    @BeforeEach
    public void setUp() {
        strategy = new SmallPricingStrategy();
    }

    @Test
    public void testCalculatePriceCorrectFor10Days() {
        //Seat Ibiza  10 days = 440
        assertEquals(440, strategy.calculatePrice(50, 10));
    }

    @Test
    public void testCalculateExtraPriceCorrectFor1Day() {
        //Seat Ibiza  1 day = 65
        assertEquals(65, strategy.calculateExtraPrice(50, 0, 1));
    }

    @Test
    public void testCalculateExtraPriceCorrectFor10Days() {
        assertEquals(650, strategy.calculateExtraPrice(50, 0, 10));
    }
}
