package com.dxc.carrental.strategy.pricing;

import com.dxc.carrental.model.enums.CarType;
import org.springframework.stereotype.Component;

@Component
public class PricingStrategyFactory {
    private final PremiumPricingStrategy premiumPricingStrategy;
    private final SmallPricingStrategy smallPricingStrategy;
    private final SUVPricingStrategy suvPricingStrategy;

    public PricingStrategyFactory(PremiumPricingStrategy premiumPricingStrategy, SmallPricingStrategy smallPricingStrategy, SUVPricingStrategy suvPricingStrategy) {
        this.premiumPricingStrategy = premiumPricingStrategy;
        this.smallPricingStrategy = smallPricingStrategy;
        this.suvPricingStrategy = suvPricingStrategy;
    }

    public PricingStrategy getPricingStrategy(CarType carType) {
        return switch (carType) {
            case PREMIUM -> premiumPricingStrategy;
            case SMALL -> smallPricingStrategy;
            case SUV -> suvPricingStrategy;
        };
    }
}
