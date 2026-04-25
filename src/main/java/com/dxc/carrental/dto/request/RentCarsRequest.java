package com.dxc.carrental.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RentCarsRequest {
    private String customerDni;
    private List<String> patents;
    private LocalDate startDate;
    private LocalDate expectedReturnDate;
}
