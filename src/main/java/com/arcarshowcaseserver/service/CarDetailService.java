package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.dto.CarDetailDTO;
import com.arcarshowcaseserver.dto.GroupedSpecsDTO;

import java.util.List;

public interface CarDetailService {

    List<CarDetailDTO> getAllDetails(Long carId);

    GroupedSpecsDTO getDetailsGrouped(Long carId);

    List<CarDetailDTO> getKeySpecifications(Long carId);

    List<CarDetailDTO> getFullSpecifications(Long carId);

    List<String> getCategories(Long carId);

    List<CarDetailDTO> searchByKey(Long carId, String key, String category);

}
