package com.dxc.carrental.strategy.pricing;

public class SUVPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(double basePrice, int days) {
        if (days <= 7) {
            return days * basePrice;
        }
        double firstTier = 7 * basePrice;
        if (days <= 30) {
            double secondTier = (days - 7) * basePrice * 0.8;
            return firstTier + secondTier;
        }
        double secondTier = 23 * basePrice * 0.8;
        double thirdTier = (days - 30) * basePrice * 0.5;
        return firstTier + secondTier + thirdTier;
    }

    @Override
    public double calculateExtraPrice(double basePrice, double extraPrice, int extraDays) {
        return extraDays * (basePrice + (0.6 * extraPrice));
    }
}
