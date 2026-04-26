package com.dxc.carrental.controller;

import com.dxc.carrental.dto.request.RentCarsRequest;
import com.dxc.carrental.dto.request.ReturnRentalRequest;
import com.dxc.carrental.model.Rental;
import com.dxc.carrental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public Rental rentCars(@Valid @RequestBody RentCarsRequest request) {
        return rentalService.rentCars(request);
    }

    @PostMapping("/return/{rentalId}")
    public Rental returnRental(@PathVariable String rentalId, @Valid @RequestBody ReturnRentalRequest request) {
        return rentalService.returnRental(rentalId, request.getActualReturnDate());
    }

    @GetMapping("/customer/{customerId}")
    public List<Rental> findByCustomerId(@PathVariable String customerId) {
        return rentalService.findRentals(customerId);
    }

}
