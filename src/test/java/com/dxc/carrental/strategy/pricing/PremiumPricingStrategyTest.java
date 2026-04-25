package com.dxc.carrental.strategy.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PremiumPricingStrategyTest {
    private PricingStrategy strategy;

    @BeforeEach
    public void setup() {
        strategy = new PremiumPricingStrategy();
    }

    @Test
    public void testCalculatePriceCorrectFor10Days() {
        //BMW 7 10 days = 3000
        assertEquals(3000, strategy.calculatePrice(300, 10));
    }

    @Test
    public void testCalculateExtraPriceCorrectFor2Days() {
        //BMW 7 2 extra days = 720
        assertEquals(720, strategy.calculateExtraPrice(300, 0, 2));
    }

    @Test
    public void testCalculateExtraPriceCorrectFor10Days() {
        assertEquals(3600, strategy.calculateExtraPrice(300, 0, 10));
    }
}
