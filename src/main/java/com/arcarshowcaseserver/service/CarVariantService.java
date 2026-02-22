package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.dto.CarVariantDTO;
import com.arcarshowcaseserver.dto.EngineCCRequest;
import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.model.Cars.CarVariant;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CarVariantService {
    List<CarVariantDTO> filterVariants(String fuel, String engine, Double maxPrice);

    List<CarVariantDTO> getBetweenPrices(double min, double max);

    List<CarVariantDTO> getUnderPrice(double priceLakhs);

    List<CarVariantDTO> getByEngine(String engine);

    List<CarVariant> getByBrandAndModelAndFuelType(String brand,String model,String fuel);

    List<CarVariant> getByBrandAndFuel(String model, String fuelType);

    Car getByVariant(String variant);
}
