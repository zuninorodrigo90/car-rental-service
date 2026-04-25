package com.dxc.carrental.strategy.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SUVPricingStrategyTest {
    private PricingStrategy strategy;

    @BeforeEach
    public void setup() {
        strategy = new SUVPricingStrategy();
    }

    @Test
    public void testCalculatePriceCorrectFor9Days() {
        //Kia Sorento 9 days = 1290
        assertEquals(1290, strategy.calculatePrice(150, 9));
    }

    @Test
    public void testCalculatePriceCorrectFor2Days() {
        //Nissan Juke 2 days = 300
        assertEquals(300, strategy.calculatePrice(150, 2));
    }

    @Test
    public void testCalculatePriceCorrectFor28Days() {
        assertEquals(3570, strategy.calculatePrice(150, 28));
    }

    @Test
    public void testCalculatePriceCorrectFor40Days() {
        assertEquals(4560, strategy.calculatePrice(150, 40));
    }

    @Test
    public void testCalculateExtraPriceCorrectFor1Day() {
        //Nissan Juke 1 day extra = 180
        assertEquals(180, strategy.calculateExtraPrice(150, 50, 1));
    }

    @Test
    public void testCalculateExtraPriceCorrectFor10Days() {
        assertEquals(1800, strategy.calculateExtraPrice(150, 50, 10));
    }
}
