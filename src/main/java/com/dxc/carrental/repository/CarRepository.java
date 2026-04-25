package com.dxc.carrental.repository;

import com.dxc.carrental.model.Car;
import com.dxc.carrental.model.enums.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    List<Car> findAllByPatentInAndAvailableTrue(List<String> patents);

    Optional<Car> findFirstByType(CarType type);
}
