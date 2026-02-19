    package com.arcarshowcaseserver.dto;


    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class CarDTO {
        private Long id;
        private String brand;
        private String model;
        private String bodyType;
        private String fuelType;
        private String priceRange;
        private double rating;
        private String image;

        public CarDTO(Long id, String brand, String model, String bodyType, String fuelType, String priceRange, double rating) {
            this.id = id;
            this.brand = brand;
            this.model = model;
            this.bodyType = bodyType;
            this.fuelType = fuelType;
            this.priceRange = priceRange;
            this.rating = rating;
        }
    }
