package com.arcarshowcaseserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupedSpecsDTO {
    private Long carId;
    private List<CarDetailDTO> keySpecifications;
    private List<CarDetailDTO> fullSpecifications;
}
