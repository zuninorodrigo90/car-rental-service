package com.dxc.carrental.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReturnRentalRequest {
    private String rentalId;
    private LocalDate actualReturnDate;
}
