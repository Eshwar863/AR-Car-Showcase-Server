package com.arcarshowcaseserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDetailDTO {
    private Long id;
    private String key;
    private String value;
    private String normalizedKey;
    private String category;


}
