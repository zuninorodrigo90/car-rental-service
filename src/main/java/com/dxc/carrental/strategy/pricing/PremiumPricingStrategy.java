package com.dxc.carrental.strategy.pricing;

import org.springframework.stereotype.Component;

@Component
public class PremiumPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(double basePrice, int days) {
        return basePrice * days;
    }

    @Override
    public double calculateExtraPrice(double basePrice, double extraPrice, int extraDays) {
        return extraDays * (1.2 * basePrice);
    }
}
