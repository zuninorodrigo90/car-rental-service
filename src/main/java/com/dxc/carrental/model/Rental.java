package com.dxc.carrental.model;

import com.dxc.carrental.model.enums.RentalStatus;

import java.time.LocalDate;
import java.util.List;

public class Rental {
    private String id;
    private Customer customer;
    private RentalStatus status;
    private LocalDate startDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private double estimatedTotalPrice;
    private double finalTotalPrice;
    private List<RentalItem> items;
}
