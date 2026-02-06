package com.arcarshowcaseserver.service.serviceImpl;

import com.arcarshowcaseserver.dto.CarDTO;
import com.arcarshowcaseserver.repository.CarRepository;
import com.arcarshowcaseserver.service.CarSearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarSearchServiceImpl implements CarSearchService {

    private final CarRepository carRepository;

    public CarSearchServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<CarDTO> simpleSearch(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return carRepository.findAllCarsAsDTO();
        }
        return carRepository.searchCars(keyword.trim());
    }

    @Override
    public List<CarDTO> advancedSearch(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return carRepository.findAllCarsAsDTO();
        }
        return carRepository.advancedSearchCars(keyword.trim());
    }

    @Override
    public List<CarDTO> multiFilterSearch(
            String keyword,
            String brand,
            String bodyType,
            String fuelType,
            Double minPrice,
            Double maxPrice,
            Double minRating
    ) {
        return carRepository.multiFilterSearch(
                keyword, brand, bodyType, fuelType,
                minPrice, maxPrice, minRating
        );
    }
}
