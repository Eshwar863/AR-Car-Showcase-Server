package com.arcarshowcaseserver.service.serviceImpl;

import com.arcarshowcaseserver.dto.CarVariantDTO;
import com.arcarshowcaseserver.exceptions.BadRequestException;
import com.arcarshowcaseserver.exceptions.InvalidInputException;
import com.arcarshowcaseserver.exceptions.ResourceNotFoundException;
import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.model.Cars.CarVariant;
import com.arcarshowcaseserver.repository.CarRepository;
import com.arcarshowcaseserver.repository.CarVariantRepository;
import com.arcarshowcaseserver.service.CarVariantService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarVariantServiceImpl implements CarVariantService {

    private final CarRepository carRepository;
    private final CarVariantRepository carVariantRepository;

    public CarVariantServiceImpl(CarRepository carRepository,CarVariantRepository carVariantRepository){
        this.carRepository = carRepository;
        this.carVariantRepository = carVariantRepository;
    }
    @Override
    public List<CarVariant> getByBrandAndModelAndFuelType(
            String brand,
            String model,
            String fuel) {

        if (brand == null || brand.isBlank()) {
            throw new InvalidInputException("Brand must not be empty");
        }

        if (model == null || model.isBlank()) {
            throw new InvalidInputException("Model must not be empty");
        }

        if (fuel == null || fuel.isBlank()) {
            throw new InvalidInputException("Fuel type must not be empty");
        }

        Car car = carRepository
                .findByBrandAndModel(brand, model)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Car not found for brand: " + brand +
                                        " and model: " + model
                        )
                );

        List<CarVariant> variants =
                carVariantRepository
                        .findByCarIdAndFuelIgnoreCase(car.getId(), fuel);

        if (variants.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No variants found for fuel type: " + fuel
            );
        }

        return variants;
    }


    @Override
    public List<CarVariant> getByBrandAndFuel(String brand, String fuelType) {

      List<Long> carIds = carRepository.findCarIdsByBrandIgnoreCase(brand);
        if (carIds.isEmpty()) {
            throw new InvalidInputException("No cars found for brand: " + brand);
        }

        List<CarVariant> variants =
                carVariantRepository.findByCarIdsAndFuel(carIds, fuelType);
        if (variants.isEmpty()) {
            throw new InvalidInputException(
                    "No variants found for brand " + brand + " with fuel type " + fuelType
            );
        }
        return variants;
    }

    @Override
    public Car getByVariant(String variant) {
        if (variant == null || variant.isBlank()) {
            throw new BadRequestException("Variant name cannot be blank");
        }

        String sanitized = sanitize(variant);

        CarVariant carVariant = carVariantRepository.findByVariant(sanitized);
        if (carVariant == null) {
            throw new ResourceNotFoundException(
                    "No variant found with name: " + sanitized
            );
        }

        return carRepository.findById(carVariant.getCar().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Car not found for variant: " + sanitized
                ));
    }


    // carvaraints
    @Override
    public List<CarVariantDTO> getByEngine(String engineCC) {

        if (engineCC == null || engineCC.isBlank()) {
            throw new InvalidInputException("Engine value cannot be empty");
        }

        String normalized = engineCC.trim().toLowerCase();

        List<CarVariantDTO> result = carVariantRepository
                .findByEngineCc(normalized)
                .stream()
                .map(this::mapToDTO)
                .toList();

        if (result.isEmpty()) {
            throw new InvalidInputException(
                    "No variants found for engine: " + engineCC
            );
        }

        return result;
    }


    @Override
    public List<CarVariantDTO> getUnderPrice(double priceLakhs) {
        return carVariantRepository.findUnderPrice(priceLakhs)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<CarVariantDTO> getBetweenPrices(double min, double max) {
        return carVariantRepository.findBetweenPrices(min, max)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<CarVariantDTO> filterVariants(String fuel, String engine, Double maxPrice) {
        return carVariantRepository.filterVariants(fuel, engine, maxPrice)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private CarVariantDTO mapToDTO(CarVariant variant) {
        return new CarVariantDTO(
                variant.getVariant(),
                variant.getPrice(),
                variant.getEngineCc(),
                variant.getFuel(),
                variant.getTransmission(),
                variant.getMileage(),
                variant.getKeySpecifications()
        );
    }

    private String sanitize(String value) {
        return value == null ? "" : value.trim();
    }
}
