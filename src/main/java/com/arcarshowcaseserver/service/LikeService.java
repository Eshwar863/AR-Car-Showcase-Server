package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.dto.CarDTO;

import java.util.Set;

public interface LikeService {

    void likeCar(Long carId);

    void unlikeCar(Long carId);

    Set<CarDTO> getUserLikedCars();

    Boolean hasUserLikedCar(Long carId);
}
