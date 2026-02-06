package com.arcarshowcaseserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarVariantDTO {

    private String variant;
    private String price;
    private String engineCc;
    private String fuel;
    private String transmission;
    private String mileage;
    private List<String> keySpecifications = new ArrayList<>();
}
