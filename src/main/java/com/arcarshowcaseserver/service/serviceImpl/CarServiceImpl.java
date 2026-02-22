package com.arcarshowcaseserver.service.serviceImpl;

import com.arcarshowcaseserver.dto.CarDTO;
import com.arcarshowcaseserver.dto.CarOptionsDTO;
import com.arcarshowcaseserver.exceptions.BadRequestException;
import com.arcarshowcaseserver.exceptions.ResourceNotFoundException;
import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.model.Cars.CarVariant;
import com.arcarshowcaseserver.repository.CarRepository;
import com.arcarshowcaseserver.repository.CarVariantRepository;
import com.arcarshowcaseserver.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class CarServiceImpl implements CarService {

    private static final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;
    private final CarVariantRepository carVariantRepository;

    public CarServiceImpl(CarRepository carRepository,
                          CarVariantRepository carVariantRepository) {
        this.carRepository = carRepository;
        this.carVariantRepository = carVariantRepository;
    }

    @Override
    public List<Car> GetAllCars() {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            throw new ResourceNotFoundException("No cars found in the database");
        }
        return cars;
    }

    @Override
    public List<Car> searchCars(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new BadRequestException("Search keyword cannot be blank");
        }
        return List.of(); // plug in search logic here
    }

    @Override
    public Car getCarsById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Car ID must be a positive number");
        }
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No car found with ID: " + id
                ));
    }

    @Override
    public List<String> getAllBrands() {
        List<String> brands = carRepository.findAll()
                .stream()
                .map(Car::getBrand)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .toList();

        if (brands.isEmpty()) {
            throw new ResourceNotFoundException("No brands found");
        }
        return brands;
    }

    @Override
    public List<String> getAllModels(String brand) {
        if (brand == null || brand.isBlank()) {
            throw new BadRequestException("Brand name cannot be blank");
        }
        List<String> models = carRepository.findModelsByBrandIgnoreCase(brand.trim());
        log.debug("Found {} models for brand '{}'", models.size(), brand);

        if (models.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No models found for brand: " + brand
            );
        }
        return models;
    }

    @Override
    public List<CarVariant> getAllVariants(String brand, String model) {
        if (brand == null || brand.isBlank()) {
            throw new BadRequestException("Brand name cannot be blank");
        }
        if (model == null || model.isBlank()) {
            throw new BadRequestException("Model name cannot be blank");
        }

        Car car = carRepository.findByBrandAndModel(brand.trim(), model.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No car found for brand: " + brand + " model: " + model
                ));

        List<CarVariant> variants = carVariantRepository.findVariantsByCarId(car.getId());
        if (variants == null || variants.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No variants found for: " + brand + " " + model
            );
        }
        return variants;
    }

    @Override
    public CarVariant getVariant(String brand, String model, String variant) {
        if (brand == null || brand.isBlank()) {
            throw new BadRequestException("Brand name cannot be blank");
        }
        if (model == null || model.isBlank()) {
            throw new BadRequestException("Model name cannot be blank");
        }
        if (variant == null || variant.isBlank()) {
            throw new BadRequestException("Variant name cannot be blank");
        }

        Car car = carRepository.findByBrandAndModel(brand.trim(), model.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No car found for brand: " + brand + " model: " + model
                ));

        return carVariantRepository
                .findByCarIdAndVariant(car.getId(), sanitize(variant))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant '" + variant + "' not found for " + brand + " " + model
                ));
    }

    @Override
    public List<CarDTO> getByBodyType(String bodyType) {
        if (bodyType == null || bodyType.isBlank()) {
            throw new BadRequestException("Body type cannot be blank");
        }
        List<CarDTO> cars = carRepository.findByBodyType(bodyType.trim());
        if (cars.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found with body type: " + bodyType
            );
        }
        return cars;
    }

    @Override
    public List<CarDTO> getByBrandAndBodyType(String brand, String bodyType) {
        if (brand == null || brand.isBlank()) {
            throw new BadRequestException("Brand cannot be blank");
        }
        if (bodyType == null || bodyType.isBlank()) {
            throw new BadRequestException("Body type cannot be blank");
        }
        List<CarDTO> cars = carRepository.findByBrandAndBodyType(
                brand.trim(), bodyType.trim()
        );
        if (cars.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found for brand: " + brand + " with body type: " + bodyType
            );
        }
        return cars;
    }

    @Override
    public List<CarDTO> getByFuelType(String fuelType) {
        if (fuelType == null || fuelType.isBlank()) {
            throw new BadRequestException("Fuel type cannot be blank");
        }
        List<CarDTO> cars = carRepository.findByFuelType(fuelType.trim());
        if (cars.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found with fuel type: " + fuelType
            );
        }
        return cars;
    }

    // ─────────────────────────────────────────────────────────
    //  Get cars by transmission type
    // ─────────────────────────────────────────────────────────
    @Override
    public List<CarDTO> getBytransmissionType(String transmissionType) {
        if (transmissionType == null || transmissionType.isBlank()) {
            throw new BadRequestException("Transmission type cannot be blank");
        }
        List<CarDTO> cars = carRepository.findByTransmissionType(
                transmissionType.trim()
        );
        if (cars.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found with transmission type: " + transmissionType
            );
        }
        return cars;
    }

    // ─────────────────────────────────────────────────────────
    //  Get cars by minimum rating
    // ─────────────────────────────────────────────────────────
    @Override
    public List<CarDTO> getByRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new BadRequestException("Rating must be between 0 and 5");
        }
        List<CarDTO> cars = carRepository.findByRating(rating);
        if (cars.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found with rating >= " + rating
            );
        }
        return cars;
    }

    // ─────────────────────────────────────────────────────────
    //  Get cars under price (in Lakhs)
    // ─────────────────────────────────────────────────────────
    @Override
    public List<CarDTO> getByPricing(double price) {
        if (price <= 0) {
            throw new BadRequestException("Price must be greater than 0");
        }
        List<CarDTO> cars = carRepository.findByUnderPrice(price);
        if (cars.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found under ₹" + price + " Lakhs"
            );
        }
        return cars;
    }

    // ─────────────────────────────────────────────────────────
    //  Get car filter options
    // ─────────────────────────────────────────────────────────
    @Override
    public CarOptionsDTO getCarOptions() {
        return new CarOptionsDTO(
                carRepository.findDistinctBrands(),
                carRepository.findDistinctBodyTypes(),
                carRepository.findDistinctFuelTypes(),
                carRepository.findDistinctTransmissionTypes()
        );
    }

    // ─────────────────────────────────────────────────────────
    //  Sanitize URL-encoded path variable
    //  Converts "GDi+IVT" → "GDi IVT"
    // ─────────────────────────────────────────────────────────
    private String sanitize(String data) {
        if (data == null) return "";
        return data.replace("+", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
