package com.dxc.carrental.service;

import com.dxc.carrental.dto.request.RentCarsRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;
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
        Customer customer = customerRepository.findByDni(rentCarsRequest.getCustomerDni()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        List<Car> cars = carRepository.findAllByPatentInAndAvailableTrue(rentCarsRequest.getPatents());

        if (cars.size() != rentCarsRequest.getPatents().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more cars were not found or are not available");
        }

        if (rentCarsRequest.getExpectedReturnDate().isBefore(rentCarsRequest.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected return date must be after start date");
        }

        long rentalDays = ChronoUnit.DAYS.between(rentCarsRequest.getStartDate(), rentCarsRequest.getExpectedReturnDate());

        List<RentalItem> items = new ArrayList<>();
        int loyaltyPoints = 0;
        double estimatedTotalPrice = 0;

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
        rental.setEarnedLoyaltyPoints(loyaltyPoints);

        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental returnRental(String rentalId, LocalDate actualReturnDate) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

        if (RentalStatus.RETURNED.equals(rental.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rental already returned");
        }

        if (actualReturnDate.isBefore(rental.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Actual return date cannot be before rental start date");
        }

        long extraDays = ChronoUnit.DAYS.between(rental.getExpectedReturnDate(), actualReturnDate);

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
        rental.setActualReturnDate(actualReturnDate);
        rental.setFinalTotalPrice(rental.getEstimatedTotalPrice() + totalSurcharge);
        rental.setStatus(RentalStatus.RETURNED);
        return rentalRepository.save(rental);

    }

    private double findSmallCarPrice() {
        return carRepository.findFirstByType(CarType.SMALL)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Small car price not found"))
                .getPrice();
    }

    public List<Rental> findRentals(String customerId) {
        List<Rental> rentalList = rentalRepository.findByCustomerId(customerId);
        if (rentalList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found for that customer id");
        }
        return rentalList;
    }
}
