package com.dxc.carrental.strategy.pricing;

public class SmallPricingStrategy implements PricingStrategy {


    @Override
    public double calculatePrice(double basePrice, int days) {
        if (days <= 7) {
            return basePrice * days;
        } else {
            return (7 * basePrice) + ((days - 7) * basePrice * 0.6);
        }
    }

    @Override
    public double calculateExtraPrice(double basePrice, double extraPrice, int extraDays) {
        return extraDays * (basePrice * 1.3);
    }
}
