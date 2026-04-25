package com.dxc.carrental.model;

import com.dxc.carrental.model.enums.CarType;
import jakarta.persistence.*;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private String patent;
    private String brand;
    private String model;
    private double price;
    @Enumerated(EnumType.STRING)
    private CarType type;
    private boolean available;
}

