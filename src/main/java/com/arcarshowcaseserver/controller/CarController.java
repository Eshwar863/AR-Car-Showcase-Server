package com.arcarshowcaseserver.controller;


import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.dto.CarDTO;
import com.arcarshowcaseserver.dto.InteractionDTO;
import com.arcarshowcaseserver.service.CarService;
import com.arcarshowcaseserver.service.RecommendationService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;
    private final RecommendationService recommendationService;

    public CarController(CarService carService, RecommendationService recommendationService){
        this.carService = carService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/allcars")
    public ResponseEntity<List<Car>> allCars(){
        return new ResponseEntity<>(carService.GetAllCars(), HttpStatus.OK);
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<Car> getCarsById(@PathVariable Long id){
        return carService.getCarsById(id);
    }

    @GetMapping("/allBrands")
    public ResponseEntity<?> allBrands(){
        return new ResponseEntity<>(carService.getAllBrands(), HttpStatus.OK);
    }

    @GetMapping("/{brand}/models")
    public ResponseEntity<?> allModels(@PathVariable(name = "brand") String brand){
        return new ResponseEntity<>(carService.getAllModels(brand), HttpStatus.OK);
    }

    @GetMapping("/{brand}/{model}/variants")
    public ResponseEntity<?> allVariants(@PathVariable(name = "brand") String brand, @PathVariable(name = "model") String model){
        return new ResponseEntity<>(carService.getAllVariants(brand,model), HttpStatus.OK);
    }

    @GetMapping("/{brand}/{model}/{variant}")
    public ResponseEntity<?> getVariant(@PathVariable(name = "brand") String brand, @PathVariable(name = "model") String model, @PathVariable(name = "variant") String variant){
        return new ResponseEntity<>(carService.getVariant(brand,model,variant), HttpStatus.OK);
    }

    @GetMapping("/body-type/{bodyType}")
    public ResponseEntity<List<CarDTO>> getByBodyType(@PathVariable String bodyType){
        return carService.getByBodyType(bodyType);
    }

    @GetMapping("brand/{brand}/body-type/{bodyType}")
    public ResponseEntity<List<CarDTO>> getByBrandAndBodyType(@PathVariable(name = "brand") String brand,@PathVariable(name = "bodyType") String bodyType){
        return carService.getByBrandAndBodyType(brand,bodyType);
    }

    @GetMapping("fuel-type/{fuelType}")
    public ResponseEntity<List<CarDTO>> getByFuelType(@PathVariable(name = "fuelType") String fuelType){
        return carService.getByFuelType(fuelType);
    }

    @GetMapping("transmission-type/{transmissionType}")
    public ResponseEntity<List<CarDTO>> getBytransmissionType(@PathVariable(name = "transmissionType") String transmissionType){
        return carService.getBytransmissionType(transmissionType);
    }

    @GetMapping("rating/{rating}")
    public ResponseEntity<List<CarDTO>> getByRating(@PathVariable(name = "rating")
                                             @Min(value = 0, message = "Rating must be greater than 0")
                                             @Max(value = 5, message = "Rating must be less than or equal to 5 ") double rating){
        return carService.getByRating(rating);
    }

    @GetMapping("pricing/{pricing}")
    public ResponseEntity<List<CarDTO>> getByPricing(@PathVariable(name = "pricing")
                                              @Min(value = 1, message = "Price must be greater than 0")
                                              @Max(value = 200, message = "Price must be less than or equal to 200 Lakhs") double pricing){
        return carService.getByPricing(pricing);
    }

    @GetMapping("/recommendations/{carId}")
    public ResponseEntity<List<Car>> getRecommendations(@PathVariable Long carId) {
        List<Car> recommendations = recommendationService.getRecommendedCars(carId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/recommendations/personalized")
    public ResponseEntity<List<Car>> getPersonalizedRecommendations(@org.springframework.security.core.annotation.AuthenticationPrincipal com.arcarshowcaseserver.security.services.UserDetailsImpl userDetails) {
        if (userDetails == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = userDetails.getId();
        List<Car> recommendations = recommendationService.getPersonalizedRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/recommendations/feedback")
    public ResponseEntity<?> recordFeedback(
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.arcarshowcaseserver.security.services.UserDetailsImpl userDetails,
            @RequestBody InteractionDTO interaction) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        recommendationService.recordInteraction(userDetails.getId(), interaction.getCarId(), interaction.getAction());
        return ResponseEntity.ok(Map.of("status", "recorded"));
    }

    @GetMapping("/options")
    public ResponseEntity<com.arcarshowcaseserver.dto.CarOptionsDTO> getCarOptions() {
        return ResponseEntity.ok(carService.getCarOptions());
    }
}
