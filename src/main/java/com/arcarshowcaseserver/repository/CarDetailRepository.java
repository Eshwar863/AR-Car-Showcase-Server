package com.arcarshowcaseserver.repository;

import com.arcarshowcaseserver.model.Cars.CarDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarDetailRepository extends JpaRepository<CarDetail, Long> {

    List<CarDetail> findByCarId(Long carId);

    List<CarDetail> findByCarIdAndCategory(Long carId, String category);

    @Query("SELECT DISTINCT d.category FROM CarDetail d WHERE d.car.id = :carId")
    List<String> findDistinctCategoriesByCarId(@Param("carId") Long carId);

    @Query("SELECT d FROM CarDetail d WHERE d.car.id = :carId AND LOWER(d.key) LIKE LOWER(CONCAT('%', :key, '%'))")
    List<CarDetail> searchByKey(@Param("carId") Long carId, @Param("key") String key);



    @Query("""
    SELECT d FROM CarDetail d
    WHERE d.car.id = :carId
    AND LOWER(d.key) LIKE LOWER(CONCAT('%', :key, '%'))
    AND d.category = 'Full Specifications'
    """)
    List<CarDetail> searchByKeyInFullSpecs(@Param("carId") Long carId, @Param("key") String key);

    @Query("""
    SELECT d FROM CarDetail d
    WHERE d.car.id = :carId
    AND LOWER(d.key) LIKE LOWER(CONCAT('%', :key, '%'))
    AND d.category = 'Key Specifications'
    """)
    List<CarDetail> searchByKeyInKeySpecs(@Param("carId") Long carId, @Param("key") String key);






}
