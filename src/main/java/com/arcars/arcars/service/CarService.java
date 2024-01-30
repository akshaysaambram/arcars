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

    public Car updateCar(@NonNull Long id, Car updatedCar) {
        Optional<Car> carObject = carRepository.findById(id);

        if (carObject.isPresent()) {
            Car car = carObject.get();

            car.setMake(updatedCar.getMake());
            car.setModel(updatedCar.getModel());
            car.setYear(updatedCar.getYear());
            car.setColor(updatedCar.getColor());
            car.setMileage(updatedCar.getMileage());
            car.setPrice(updatedCar.getPrice());
            car.setFuelType(updatedCar.getFuelType());
            car.setTransmission(updatedCar.getTransmission());
            car.setEngine(updatedCar.getEngine());
            car.setHorsepower(updatedCar.getHorsepower());
            car.setOwners(updatedCar.getOwners());
            car.setImage(updatedCar.getImage());

            return carRepository.save(car);

        } else {
            throw new IllegalArgumentException("Car with id " + id + " not found");
        }
    }

    public Boolean deleteCarById(@NonNull Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public Boolean deleteAllCars() {
        if (carRepository.count() != 0) {
            carRepository.deleteAll();
            return true;
        }

        return false;
    }

}
