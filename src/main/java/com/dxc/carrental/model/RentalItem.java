package com.dxc.carrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class RentalItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    private Car car;
    private double estimatedTotalPrice;
    private double surcharge;

}
