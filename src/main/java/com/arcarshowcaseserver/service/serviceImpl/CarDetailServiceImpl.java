package com.arcarshowcaseserver.service.serviceImpl;

import com.arcarshowcaseserver.dto.CarDetailDTO;
import com.arcarshowcaseserver.dto.GroupedSpecsDTO;
import com.arcarshowcaseserver.exceptions.BadRequestException;
import com.arcarshowcaseserver.exceptions.ResourceNotFoundException;
import com.arcarshowcaseserver.model.Cars.CarDetail;
import com.arcarshowcaseserver.repository.CarDetailRepository;
import com.arcarshowcaseserver.repository.CarRepository;
import com.arcarshowcaseserver.service.CarDetailService;
import com.arcarshowcaseserver.service.KeyNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarDetailServiceImpl implements CarDetailService {

    private final CarDetailRepository carDetailRepository;
    private final CarRepository carRepository;

    @Override
    public List<CarDetailDTO> getAllDetails(Long carId) {
        validateCarExists(carId);
        return carDetailRepository.findByCarId(carId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GroupedSpecsDTO getDetailsGrouped(Long carId) {
        validateCarExists(carId);
        List<CarDetail> all = carDetailRepository.findByCarId(carId);

        List<CarDetailDTO> keySpecs = all.stream()
                .filter(d -> "Key Specifications".equals(d.getCategory()))
                .map(this::toDTO)
                .collect(Collectors.toList());

        List<CarDetailDTO> fullSpecs = all.stream()
                .filter(d -> "Full Specifications".equals(d.getCategory()))
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new GroupedSpecsDTO(carId, keySpecs, fullSpecs);
    }


    @Override
    public List<CarDetailDTO> getKeySpecifications(Long carId) {
        validateCarExists(carId);
        return carDetailRepository
                .findByCarIdAndCategory(carId, "Key Specifications")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDetailDTO> getFullSpecifications(Long carId) {
        validateCarExists(carId);
        return carDetailRepository
                .findByCarIdAndCategory(carId, "Full Specifications")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCategories(Long carId) {
        validateCarExists(carId);
        return carDetailRepository.findDistinctCategoriesByCarId(carId);
    }

    @Override
    public List<CarDetailDTO> searchByKey(Long carId, String key, String category) {
        validateCarExists(carId);

        if (key == null || key.isBlank()) {
            throw new BadRequestException("Search key cannot be empty");
        }

        List<CarDetail> results = switch (category.toLowerCase()) {
            case "full specifications", "full" ->
                    carDetailRepository.searchByKeyInFullSpecs(carId, key);
            case "key specifications", "key" ->
                    carDetailRepository.searchByKeyInKeySpecs(carId, key);
            default ->
                    deduplicateByKey(carDetailRepository.searchByKey(carId, key));
        };

        if (results.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No spec found matching '" + key + "' for car ID: " + carId
            );
        }

        return results.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    private CarDetailDTO toDTO(CarDetail detail) {
        return new CarDetailDTO(
                detail.getId(),
                detail.getKey(),
                KeyNormalizer.normalize(detail.getKey()),
                detail.getValue(),
                detail.getCategory()
        );
    }



    private List<CarDetail> deduplicateByKey(List<CarDetail> details) {
        Map<String, CarDetail> map = new LinkedHashMap<>();
        for (CarDetail d : details) {
            if (!map.containsKey(d.getKey()) ||
                    "Full Specifications".equals(d.getCategory())) {
                map.put(d.getKey(), d);
            }
        }
        return new ArrayList<>(map.values());
    }


    private void validateCarExists(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new ResourceNotFoundException("Car not found with ID: " + carId);
        }
    }
}
