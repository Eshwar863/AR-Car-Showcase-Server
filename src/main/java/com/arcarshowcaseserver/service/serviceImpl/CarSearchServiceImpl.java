package com.arcarshowcaseserver.service.serviceImpl;

import com.arcarshowcaseserver.dto.CarDTO;
import com.arcarshowcaseserver.exceptions.BadRequestException;
import com.arcarshowcaseserver.exceptions.ResourceNotFoundException;
import com.arcarshowcaseserver.repository.CarRepository;
import com.arcarshowcaseserver.service.CarSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CarSearchServiceImpl implements CarSearchService {

    private static final Logger log = LoggerFactory.getLogger(CarSearchServiceImpl.class);
    private final CarRepository carRepository;

    public CarSearchServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<CarDTO> simpleSearch(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            log.debug("No keyword provided — returning all cars");
            List<CarDTO> all = carRepository.findAllCarsAsDTO();
            if (all.isEmpty()) {
                throw new ResourceNotFoundException("No cars found in the database");
            }
            return all;
        }

        log.debug("Simple search for keyword: '{}'", keyword.trim());
        List<CarDTO> results = carRepository.searchCars(keyword.trim());
        if (results.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found matching: '" + keyword.trim() + "'"
            );
        }
        return results;
    }

    @Override
    public List<CarDTO> advancedSearch(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            log.debug("No keyword provided — returning all cars");
            List<CarDTO> all = carRepository.findAllCarsAsDTO();
            if (all.isEmpty()) {
                throw new ResourceNotFoundException("No cars found in the database");
            }
            return all;
        }

        log.debug("Advanced search for keyword: '{}'", keyword.trim());
        List<CarDTO> results = carRepository.advancedSearchCars(keyword.trim());
        if (results.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found matching: '" + keyword.trim() + "'"
            );
        }
        return results;
    }

    @Override
    public List<CarDTO> multiFilterSearch(
            String keyword,
            String brand,
            String bodyType,
            String fuelType,
            Double minPrice,
            Double maxPrice,
            Double minRating) {

        if (minPrice != null && minPrice < 0) {
            throw new BadRequestException("Minimum price cannot be negative");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new BadRequestException("Maximum price cannot be negative");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new BadRequestException(
                    "Minimum price (" + minPrice + ") cannot be greater than maximum price (" + maxPrice + ")"
            );
        }

        if (minRating != null && (minRating < 0 || minRating > 5)) {
            throw new BadRequestException("Rating must be between 0 and 5");
        }

        String kw         = sanitize(keyword);
        String br         = sanitize(brand);
        String bt         = sanitize(bodyType);
        String ft         = sanitize(fuelType);

        log.debug("Multi-filter search — keyword: '{}', brand: '{}', bodyType: '{}', " +
                        "fuelType: '{}', minPrice: {}, maxPrice: {}, minRating: {}",
                kw, br, bt, ft, minPrice, maxPrice, minRating);

        List<CarDTO> results = carRepository.multiFilterSearch(
                kw.isEmpty()  ? null : kw,
                br.isEmpty()  ? null : br,
                bt.isEmpty()  ? null : bt,
                ft.isEmpty()  ? null : ft,
                minPrice,
                maxPrice,
                minRating
        );

        if (results.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found matching the given filters"
            );
        }
        return results;
    }

    private String sanitize(String value) {
        return value == null ? "" : value.trim();
    }
}
