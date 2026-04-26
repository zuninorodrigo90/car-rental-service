package com.dxc.carrental.service;

import com.dxc.carrental.model.Car;
import com.dxc.carrental.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository repository;

    public CarService(CarRepository repository) {
        this.repository = repository;
    }

    public List<Car> getCars() {
        return repository.findAll();
    }
}
