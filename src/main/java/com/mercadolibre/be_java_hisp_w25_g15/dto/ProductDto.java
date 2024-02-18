package com.mercadolibre.be_java_hisp_w25_g15.dto;

public record ProductDto(
        int product_id,
        String product_name,
        String type,
        String brand,
        String color,
        String notes
) {
}
