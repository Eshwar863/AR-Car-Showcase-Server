package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.dto.CarDTO;
import java.util.List;

public interface CarSearchService {
    List<CarDTO> simpleSearch(String keyword);

    List<CarDTO> advancedSearch(String keyword);

    List<CarDTO> multiFilterSearch(
            String keyword,
            String brand,
            String bodyType,
            String fuelType,
            Double minPrice,
            Double maxPrice,
            Double minRating
    );
}
