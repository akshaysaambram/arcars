package com.arcars.arcars.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.arcars.arcars.model.Car;
import com.arcars.arcars.repository.CarRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(@NonNull Long id) {
        return carRepository.findById(id);
    }

    public Car addCar(@NonNull Car car) {
        return carRepository.save(car);
    }

}
