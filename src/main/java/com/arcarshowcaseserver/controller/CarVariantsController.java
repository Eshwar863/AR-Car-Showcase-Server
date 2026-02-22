package com.arcarshowcaseserver.controller;

import com.arcarshowcaseserver.dto.CarVariantDTO;
import com.arcarshowcaseserver.exceptions.BadRequestException;
import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.model.Cars.CarVariant;
import com.arcarshowcaseserver.service.CarVariantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/carvariants")
public class CarVariantsController {
    private final CarVariantService carVariantService;

    public CarVariantsController(CarVariantService carVariantService){
        this.carVariantService = carVariantService;
    }

    @GetMapping("/variant/{variant}")
    public ResponseEntity<Car> getByVariant(@PathVariable String variant) {
        if (variant == null || variant.isBlank()) {
            throw new BadRequestException("Variant name cannot be blank");
        }
        return ResponseEntity.ok(carVariantService.getByVariant(variant));
    }

    @GetMapping("/brand/{brand}/model/{model}/fuel/{fuelType}")
    public ResponseEntity<List<CarVariant>> getByBrandAndModelAndFuelType(
            @PathVariable String brand , @PathVariable String model, @PathVariable String fuelType) {
        if (brand == null || brand.isBlank()) {
            throw new BadRequestException("Brand cannot be blank");
        }
        if (model == null || model.isBlank()) {
            throw new BadRequestException("Model cannot be blank");
        }
        if (fuelType == null || fuelType.isBlank()) {
            throw new BadRequestException("Fuel type cannot be blank");
        }
        return ResponseEntity.ok(
                carVariantService.getByBrandAndModelAndFuelType(brand,model,fuelType)
        );
    }

    @GetMapping("/brand/{brand}/fuel/{fuelType}")
    public ResponseEntity<List<CarVariant>> getByModelAndFuelType(
            @PathVariable String brand , @PathVariable String fuelType) {
        if (brand == null || brand.isBlank()) {
            throw new BadRequestException("Brand cannot be blank");
        }
        if (fuelType == null || fuelType.isBlank()) {
            throw new BadRequestException("Fuel type cannot be blank");
        }
        return ResponseEntity.ok(
                carVariantService.getByBrandAndFuel(brand,fuelType)
        );
    }

    @GetMapping("/engineCC")
    public ResponseEntity<List<CarVariantDTO>> getByEngine(
            @RequestParam String engineCC) {
        if (engineCC == null || engineCC.isBlank()) {
            throw new BadRequestException("Engine CC value cannot be blank");
        }
        return ResponseEntity.ok(
                carVariantService.getByEngine(engineCC)
        );
    }


    @GetMapping("/price/under/{priceLakhs}")
    public ResponseEntity<List<CarVariantDTO>> getUnderPrice(
            @PathVariable double priceLakhs) {
        if (priceLakhs <= 0) {
            throw new BadRequestException("Price must be greater than 0");
        }
        if (priceLakhs > 200) {
            throw new BadRequestException("Price must be at most 200 Lakhs");
        }
        return ResponseEntity.ok(
                carVariantService.getUnderPrice(priceLakhs)
        );
    }
    @GetMapping("/price/between")
    public ResponseEntity<List<CarVariantDTO>> getBetweenPrices(
            @RequestParam double min,
            @RequestParam double max) {

        return ResponseEntity.ok(
                carVariantService.getBetweenPrices(min, max)
        );
    }
    @GetMapping("/filter")
    public ResponseEntity<List<CarVariantDTO>> filterVariants(
            @RequestParam(required = false) String fuel,
            @RequestParam(required = false) String engine,
            @RequestParam(required = false) Double maxPrice) {
        if (maxPrice != null && maxPrice <= 0) {
            throw new BadRequestException("Max price must be greater than 0");
        }
        if (maxPrice != null && maxPrice > 200) {
            throw new BadRequestException("Max price must be at most 200 Lakhs");
        }
        return ResponseEntity.ok(
                carVariantService.filterVariants(fuel, engine, maxPrice)
        );
    }

}
