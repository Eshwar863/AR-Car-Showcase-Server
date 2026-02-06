package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.dto.CarDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CarService {
    List<Car> GetAllCars();

    ResponseEntity<?> getAllBrands();

    ResponseEntity<?> getAllModels(String brand);

    ResponseEntity<?> getAllVariants(String brand, String model);

    ResponseEntity<?> getVariant(String brand, String model, String variant);

    ResponseEntity<List<CarDTO>>  getByBodyType(String bodyType);

    ResponseEntity<List<CarDTO>>  getByBrandAndBodyType(String brand, String bodyType);

    ResponseEntity<List<CarDTO>>  getByFuelType(String fuelType);

    ResponseEntity<List<CarDTO>>  getBytransmissionType(String transmissionType);

    ResponseEntity<List<CarDTO>>  getByRating(double rating);

    ResponseEntity<List<CarDTO>>  getByPricing(double pricing);

    List<Car> searchCars(String keyword);

    ResponseEntity<Car> getCarsById(Long id);
}
