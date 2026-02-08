package com.arcarshowcaseserver.controller;

import com.arcarshowcaseserver.dto.CarVariantDTO;
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
    public ResponseEntity<Car> getByVariant(
             @PathVariable String variant) {
        return carVariantService.getByVariant(variant);
    }

    @GetMapping("/brand/{brand}/model/{model}/fuel/{fuelType}")
    public ResponseEntity<List<CarVariant>> getByBrandAndModelAndFuelType(
            @PathVariable String brand , @PathVariable String model, @PathVariable String fuelType) {

        return ResponseEntity.ok(
                carVariantService.getByBrandAndModelAndFuelType(brand,model,fuelType)
        );
    }

    @GetMapping("/brand/{brand}/fuel/{fuelType}")
    public ResponseEntity<List<CarVariant>> getByModelAndFuelType(
            @PathVariable String brand , @PathVariable String fuelType) {

        return ResponseEntity.ok(
                carVariantService.getByBrandAndFuel(brand,fuelType)
        );
    }

    @GetMapping("/engineCC")
    public ResponseEntity<List<CarVariantDTO>> getByEngine(
            @RequestParam String engineCC) {

        return ResponseEntity.ok(
                carVariantService.getByEngine(engineCC)
        );
    }


    @GetMapping("/price/under/{priceLakhs}")
    public ResponseEntity<List<CarVariantDTO>> getUnderPrice(
            @PathVariable double priceLakhs) {

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

        return ResponseEntity.ok(
                carVariantService.filterVariants(fuel, engine, maxPrice)
        );
    }

}
