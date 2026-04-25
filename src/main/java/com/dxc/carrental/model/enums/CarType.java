package com.dxc.carrental.model.enums;

import lombok.Getter;

@Getter
public enum CarType {
    PREMIUM(5),
    SUV(3),
    SMALL(1);

    private final int loyaltyPoints;

    CarType(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

}
