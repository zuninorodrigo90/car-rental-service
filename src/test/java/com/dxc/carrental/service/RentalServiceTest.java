package com.dxc.carrental.service;

import com.dxc.carrental.dto.request.RentCarsRequest;
import com.dxc.carrental.model.Car;
import com.dxc.carrental.model.Customer;
import com.dxc.carrental.model.Rental;
import com.dxc.carrental.model.enums.CarType;
import com.dxc.carrental.model.enums.RentalStatus;
import com.dxc.carrental.repository.CarRepository;
import com.dxc.carrental.repository.CustomerRepository;
import com.dxc.carrental.repository.RentalRepository;
import com.dxc.carrental.strategy.pricing.PricingStrategy;
import com.dxc.carrental.strategy.pricing.PricingStrategyFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PricingStrategyFactory pricingStrategyFactory;
    @Mock
    private PricingStrategy pricingStrategy;
    @InjectMocks
    private RentalService rentalService;

    @Test
    void rentCarsShouldCreateRentalSuccessfully() {
        RentCarsRequest request = new RentCarsRequest();
        request.setCustomerDni("12345678");
        request.setPatents(List.of("AAA111"));
        request.setStartDate(LocalDate.of(2026, 4, 25));
        request.setExpectedReturnDate(LocalDate.of(2026, 4, 30));
        Customer customer = new Customer();
        customer.setDni("12345678");
        customer.setLoyaltyPoints(0);
        Car car = new Car();
        car.setPatent("AAA111");
        car.setBrand("BMW");
        car.setModel("7");
        car.setPrice(300);
        car.setType(CarType.PREMIUM);
        car.setAvailable(true);
        when(customerRepository.findByDni("12345678")).thenReturn(Optional.of(customer));
        when(carRepository.findAllByPatentInAndAvailableTrue(List.of("AAA111"))).thenReturn(List.of(car));
        when(pricingStrategyFactory.getPricingStrategy(CarType.PREMIUM)).thenReturn(pricingStrategy);
        when(pricingStrategy.calculatePrice(300, 5)).thenReturn(1500.0);
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Rental result = rentalService.rentCars(request);
        assertEquals(RentalStatus.CREATED, result.getStatus());
        assertEquals(customer, result.getCustomer());
        assertEquals(1, result.getItems().size());
        assertEquals(1500.0, result.getEstimatedTotalPrice());
        assertEquals(1500.0, result.getFinalTotalPrice());
        assertEquals(5, result.getEarnedLoyaltyPoints());
        assertEquals(5, customer.getLoyaltyPoints());
        assertFalse(car.isAvailable());
        verify(rentalRepository).save(any(Rental.class));
    }

}
