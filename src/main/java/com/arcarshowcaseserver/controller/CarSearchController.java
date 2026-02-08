package com.arcarshowcaseserver.controller;

import com.arcarshowcaseserver.dto.CarDTO;
import com.arcarshowcaseserver.service.CarSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search/cars")
@CrossOrigin(origins = "*")
public class CarSearchController {

    private final CarSearchService carSearchService;

    public CarSearchController(CarSearchService carSearchService) {
        this.carSearchService = carSearchService;
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> searchCars(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(carSearchService.simpleSearch(keyword));
    }

    @GetMapping("/advanced")
    public ResponseEntity<List<CarDTO>> advancedSearch(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(carSearchService.advancedSearch(keyword));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CarDTO>> multiFilterSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String bodyType,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating
    ) {
        return ResponseEntity.ok(
                carSearchService.multiFilterSearch(
                        keyword, brand, bodyType, fuelType,
                        minPrice, maxPrice, minRating
                )
        );
    }
}
