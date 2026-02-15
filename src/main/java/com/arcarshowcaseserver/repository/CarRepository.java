package com.arcarshowcaseserver.repository;

import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.dto.CarDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {


    @Query("SELECT DISTINCT LOWER(c.model) FROM Car c WHERE LOWER(c.brand) = LOWER(:brand)")
    List<String> findModelsByBrandIgnoreCase(@Param("brand") String brand);

    @Query("""
       SELECT c
       FROM Car c
       WHERE LOWER(c.brand) = LOWER(:brand)
         AND LOWER(c.model) = LOWER(:model)
       ORDER BY c.id ASC
       """)
    Optional<Car> findByBrandAndModel(
            @Param("brand") String brand,
            @Param("model") String model
    );

    @Query("""
        SELECT c.id
        FROM Car c
        WHERE LOWER(c.brand) = LOWER(:brand)
    """)
    List<Long> findCarIdsByBrandIgnoreCase(@Param("brand") String brand);


    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        ORDER BY c.rating DESC
    """)
    List<CarDTO> findAllCarsAsDTO();

    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        WHERE LOWER(c.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.model) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.bodyType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.fuelType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.transmissionType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.priceRange) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY c.rating DESC
    """)
    List<CarDTO> searchCars(@Param("keyword") String keyword);

    @Query("""
        SELECT DISTINCT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        LEFT JOIN c.variants v
        LEFT JOIN c.details d
        WHERE LOWER(c.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.model) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.bodyType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.fuelType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.transmissionType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.priceRange) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.variant) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.fuel) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.transmission) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.engineCc) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.key) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.value) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY c.rating DESC
    """)
    List<CarDTO> advancedSearchCars(@Param("keyword") String keyword);

    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        WHERE (:keyword IS NULL OR 
               LOWER(c.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
               LOWER(c.model) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
               LOWER(c.bodyType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
               LOWER(c.fuelType) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:brand IS NULL OR LOWER(c.brand) = LOWER(:brand))
          AND (:bodyType IS NULL OR LOWER(c.bodyType) = LOWER(:bodyType))
          AND (:fuelType IS NULL OR LOWER(c.fuelType) = LOWER(:fuelType))
          AND (:minPrice IS NULL OR c.minPriceLakhs >= :minPrice)
          AND (:maxPrice IS NULL OR c.maxPriceLakhs <= :maxPrice)
          AND (:minRating IS NULL OR c.rating >= :minRating)
        ORDER BY c.rating DESC
    """)
    List<CarDTO> multiFilterSearch(
            @Param("keyword") String keyword,
            @Param("brand") String brand,
            @Param("bodyType") String bodyType,
            @Param("fuelType") String fuelType,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minRating") Double minRating
    );

    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        WHERE LOWER(c.bodyType) = LOWER(:bodyType)
        ORDER BY c.rating DESC
    """)
    List<CarDTO> findByBodyType(@Param("bodyType") String bodyType);

    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        WHERE LOWER(c.brand) = LOWER(:brand)
          AND LOWER(c.bodyType) = LOWER(:bodyType)
        ORDER BY c.rating DESC
    """)
    List<CarDTO> findByBrandAndBodyType(
            @Param("brand") String brand,
            @Param("bodyType") String bodyType
    );


    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        WHERE LOWER(c.fuelType) = LOWER(:fuelType)
        ORDER BY c.rating DESC
    """)
    List<CarDTO> findByFuelType(@Param("fuelType") String fuelType);

    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        WHERE LOWER(c.transmissionType) = LOWER(:transmissionType)
        ORDER BY c.rating DESC
    """)
    List<CarDTO> findByTransmissionType(
            @Param("transmissionType") String transmissionType
    );

    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        WHERE c.rating >= :rating
        ORDER BY c.rating DESC
    """)
    List<CarDTO> findByRating(@Param("rating") double rating);

    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        WHERE c.minPriceLakhs <= :price
        ORDER BY c.minPriceLakhs ASC
    """)
    List<CarDTO> findByUnderPrice(@Param("price") double price);

    @EntityGraph(attributePaths = {"variants"})
    @Query("SELECT c FROM Car c WHERE c.id IN :ids")
    List<Car> findByIdsWithVariants(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"details"})
    @Query("SELECT c FROM Car c WHERE c.id IN :ids")
    List<Car> findByIdsWithDetails(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"images"})
    @Query("SELECT c FROM Car c WHERE c.id IN :ids")
    List<Car> findByIdsWithImages(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"colors"})
    @Query("SELECT c FROM Car c WHERE c.id IN :ids")
    List<Car> findByIdsWithColors(@Param("ids") List<Long> ids);


    @Query("""
        SELECT DISTINCT c.id
        FROM Car c
        LEFT JOIN c.variants v
        LEFT JOIN c.details d
        WHERE LOWER(c.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.model) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.bodyType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.fuelType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.transmissionType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.variant) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.fuel) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.transmission) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.engineCc) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.key) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(d.value) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Long> searchCarIds(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT c.brand FROM Car c ORDER BY c.brand")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT c.bodyType FROM Car c ORDER BY c.bodyType")
    List<String> findDistinctBodyTypes();

    @Query("SELECT DISTINCT c.fuelType FROM Car c ORDER BY c.fuelType")
    List<String> findDistinctFuelTypes();

    @Query("SELECT DISTINCT c.transmissionType FROM Car c ORDER BY c.transmissionType")
    List<String> findDistinctTransmissionTypes();

    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id,
            c.brand,
            c.model,
            c.bodyType,
            c.fuelType,
            c.priceRange,
            c.rating
        )
        FROM Car c
        WHERE LOWER(c.brand) IN (:brands)
           OR LOWER(c.bodyType) IN (:bodyTypes)
           OR LOWER(c.fuelType) IN (:fuelTypes)
           OR LOWER(c.transmissionType) IN (:transmissions)
           OR LOWER(c.transmissionType) = LOWER(:drivingCondition)
           OR (c.maxPriceLakhs <= :maxBudget)
        ORDER BY (
            (CASE WHEN LOWER(c.brand) IN (:brands) THEN 3 ELSE 0 END) +
            (CASE WHEN LOWER(c.bodyType) IN (:bodyTypes) THEN 2 ELSE 0 END) +
            (CASE WHEN LOWER(c.fuelType) IN (:fuelTypes) THEN 1.5 ELSE 0 END) +
            (CASE WHEN LOWER(c.transmissionType) IN (:transmissions) THEN 1.5 ELSE 0 END) +
            (CASE WHEN LOWER(c.transmissionType) = LOWER(:drivingCondition) THEN 1 ELSE 0 END) +
            (CASE WHEN c.maxPriceLakhs <= :maxBudget THEN 1.5 ELSE 0 END)
        ) DESC
    """)
    List<CarDTO> findCandidateCars(
            @Param("brands") List<String> brands,
            @Param("bodyTypes") List<String> bodyTypes,
            @Param("fuelTypes") List<String> fuelTypes,
            @Param("transmissions") List<String> transmissions,
            @Param("drivingCondition") String drivingCondition,
            @Param("maxBudget") Double maxBudget
    );
}
