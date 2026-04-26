package com.dxc.carrental.model;

import com.dxc.carrental.model.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private RentalStatus status;
    private LocalDate startDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private double estimatedTotalPrice;
    private double finalTotalPrice;
    @OneToMany(cascade = CascadeType.ALL)
    private List<RentalItem> items;
    private int earnedLoyaltyPoints;
}
