package com.dxc.carrental.model.enums;

import com.dxc.carrental.strategy.pricing.PremiumPricingStrategy;
import com.dxc.carrental.strategy.pricing.PricingStrategy;
import com.dxc.carrental.strategy.pricing.SUVPricingStrategy;
import com.dxc.carrental.strategy.pricing.SmallPricingStrategy;
import lombok.Getter;

@Getter
public enum CarType {
    PREMIUM(new PremiumPricingStrategy(), 5),
    SUV(new SUVPricingStrategy(), 3),
    SMALL(new SmallPricingStrategy(), 1);

    private final PricingStrategy pricingStrategy;
    private final int loyaltyPoints;

    CarType(PricingStrategy pricingStrategy, int loyaltyPoints) {
        this.pricingStrategy = pricingStrategy;
        this.loyaltyPoints = loyaltyPoints;
    }

}
