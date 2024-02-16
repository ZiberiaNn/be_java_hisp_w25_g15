package com.mercadolibre.be_java_hisp_w25_g15.dto;

public record ProductDto(
        int productId,
        String productName,
        String type,
        String brand,
        String color,
        String notes
) {
}
