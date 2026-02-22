package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.dto.CarDTO;
import com.arcarshowcaseserver.dto.CarOptionsDTO;
import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.model.Cars.CarVariant;

import java.util.List;

public interface CarService {
    List<Car> GetAllCars();

    List<Car> searchCars(String keyword);

    Car getCarsById(Long id);

    List<String> getAllBrands();

    List<String> getAllModels(String brand);

    List<CarVariant> getAllVariants(String brand, String model);

    CarVariant getVariant(String brand, String model, String variant);

    List<CarDTO> getByBodyType(String bodyType);

    List<CarDTO> getByBrandAndBodyType(String brand, String bodyType);

    List<CarDTO> getByFuelType(String fuelType);

    List<CarDTO> getBytransmissionType(String transmissionType);

    List<CarDTO> getByRating(double rating);

    List<CarDTO> getByPricing(double price);

    CarOptionsDTO getCarOptions();
}
