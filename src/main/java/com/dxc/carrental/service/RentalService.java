package com.dxc.carrental.service;

import com.dxc.carrental.dto.request.RentCarsRequest;
import com.dxc.carrental.dto.request.ReturnRentalRequest;
import com.dxc.carrental.model.Car;
import com.dxc.carrental.model.Customer;
import com.dxc.carrental.model.Rental;
import com.dxc.carrental.model.RentalItem;
import com.dxc.carrental.model.enums.CarType;
import com.dxc.carrental.model.enums.RentalStatus;
import com.dxc.carrental.repository.CarRepository;
import com.dxc.carrental.repository.CustomerRepository;
import com.dxc.carrental.repository.RentalRepository;
import com.dxc.carrental.strategy.pricing.PricingStrategy;
import com.dxc.carrental.strategy.pricing.PricingStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {
    private RentalRepository rentalRepository;
    private CustomerRepository customerRepository;
    private CarRepository carRepository;
    private final PricingStrategyFactory pricingStrategyFactory;

    public RentalService(
            CarRepository carRepository,
            CustomerRepository customerRepository,
            RentalRepository rentalRepository,
            PricingStrategyFactory pricingStrategyFactory) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.rentalRepository = rentalRepository;
        this.pricingStrategyFactory = pricingStrategyFactory;
    }

    @Transactional
    public Rental rentCars(RentCarsRequest rentCarsRequest) {
        Customer customer = customerRepository.findByDni(rentCarsRequest.getCustomerDni()).orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Car> cars = carRepository.findAllByPatentInAndAvailableTrue(rentCarsRequest.getPatents());

        List<RentalItem> items = new ArrayList<>();
        int loyaltyPoints = 0;
        double estimatedTotalPrice = 0;

        if (cars.isEmpty()) {
            throw new RuntimeException("Cars not found or not available");
        }

        long rentalDays = ChronoUnit.DAYS.between(rentCarsRequest.getStartDate(), rentCarsRequest.getExpectedReturnDate());

        for (Car car : cars) {
            PricingStrategy pricingStrategy = pricingStrategyFactory.getPricingStrategy(car.getType());
            double estimatedItemPrice = pricingStrategy.calculatePrice(car.getPrice(), (int) rentalDays);

            RentalItem rentalItem = new RentalItem();
            rentalItem.setCar(car);
            rentalItem.setEstimatedTotalPrice(estimatedItemPrice);
            rentalItem.setSurcharge(0);

            items.add(rentalItem);

            car.setAvailable(false);
            loyaltyPoints += car.getType().getLoyaltyPoints();
            estimatedTotalPrice += estimatedItemPrice;
        }

        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + loyaltyPoints);

        Rental rental = new Rental();
        rental.setCustomer(customer);
        rental.setItems(items);
        rental.setStatus(RentalStatus.CREATED);
        rental.setStartDate(rentCarsRequest.getStartDate());
        rental.setExpectedReturnDate(rentCarsRequest.getExpectedReturnDate());
        rental.setEstimatedTotalPrice(estimatedTotalPrice);
        rental.setFinalTotalPrice(estimatedTotalPrice);
        rental.setLoyaltyPoints(loyaltyPoints);

        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental returnRental(ReturnRentalRequest returnRentalRequest) {
        Rental rental = rentalRepository.findById(returnRentalRequest.getRentalId()).orElseThrow(() -> new RuntimeException("Rental not found"));

        if (RentalStatus.RETURNED.equals(rental.getStatus())) {
            throw new RuntimeException("Rental already returned");
        }

        long extraDays = ChronoUnit.DAYS.between(rental.getExpectedReturnDate(), returnRentalRequest.getActualReturnDate());

        if (extraDays < 0) {
            extraDays = 0;
        }

        double smallPrice = findSmallCarPrice();
        double totalSurcharge = 0;

        for (RentalItem rentalItem : rental.getItems()) {
            Car car = rentalItem.getCar();
            PricingStrategy pricingStrategy = pricingStrategyFactory.getPricingStrategy(car.getType());
            double surcharge = pricingStrategy.calculateExtraPrice(car.getPrice(), smallPrice, (int) extraDays);
            rentalItem.setSurcharge(surcharge);
            totalSurcharge += surcharge;
            car.setAvailable(true);
        }
        rental.setActualReturnDate(returnRentalRequest.getActualReturnDate());
        rental.setFinalTotalPrice(rental.getEstimatedTotalPrice() + totalSurcharge);
        rental.setStatus(RentalStatus.RETURNED);
        return rentalRepository.save(rental);

    }

    private double findSmallCarPrice() {
        return carRepository.findFirstByType(CarType.SMALL)
                .orElseThrow(() -> new RuntimeException("Small car price not found"))
                .getPrice();
    }
}
