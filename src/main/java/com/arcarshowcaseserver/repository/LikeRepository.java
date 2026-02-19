package com.arcarshowcaseserver.repository;

import com.arcarshowcaseserver.dto.CarDTO;
import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.model.Cars.Like;
import com.arcarshowcaseserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByCarAndUser(Car car, User user);


    @Query("""
        SELECT new com.arcarshowcaseserver.dto.CarDTO(
            c.id, 
            c.brand, 
            c.model, 
            c.bodyType, 
            c.fuelType, 
            c.priceRange, 
            c.rating,
            (SELECT ci.imageUrl FROM CarImage ci WHERE ci.car.id = c.id ORDER BY CASE WHEN LOWER(ci.type) = 'main' THEN 1 WHEN LOWER(ci.type) = 'exterior' THEN 2 WHEN LOWER(ci.type) = 'primary' THEN 3 ELSE 4 END ASC, ci.id ASC LIMIT 1)
        )
        FROM Like l
        JOIN l.car c
        WHERE l.user.id = :userId
        ORDER BY l.likedAt DESC
        """)
    Set<CarDTO> findLikedCarsByUserId(@Param("userId") Long userId);

    boolean existsByCarAndUser(Car car, User user);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.car.id = :carId AND l.user.id = :userId")
    boolean existsByCarIdAndUserId(@Param("carId") Long carId, @Param("userId") Long userId);
}
