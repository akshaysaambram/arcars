package com.arcars.arcars.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.arcars.arcars.model.Car;
import com.arcars.arcars.service.CarService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/cars")
    @ResponseBody
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/car")
    @ResponseBody
    public Optional<Car> getCarById(@RequestParam Long id) {
        return carService.getCarById(id);
    }

    @PreAuthorize("hasRole('ROLE_DEALER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/cars")
    @ResponseBody
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @PreAuthorize("hasRole('ROLE_DEALER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/cars/car")
    @ResponseBody
    public Car updateCar(@RequestParam Long id, @RequestBody Car updatedCar) {
        return carService.updateCar(id, updatedCar);
    }

    @PreAuthorize("hasRole('ROLE_DEALER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/cars/car")
    @ResponseBody
    public Boolean deleteCarById(@RequestParam Long id) {
        return carService.deleteCarById(id);
    }

    @PreAuthorize("hasRole('ROLE_DEALER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/cars")
    @ResponseBody
    public Boolean deleteAllCars() {
        return carService.deleteAllCars();
    }

}
