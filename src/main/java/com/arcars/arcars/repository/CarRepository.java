package com.arcars.arcars.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arcars.arcars.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

}
