package com.dxc.carrental.strategy.pricing;

public interface PricingStrategy {
    double calculatePrice(double basePrice, int days);

    double calculateExtraPrice(double basePrice, double extraPrice, int extraDays);

}
