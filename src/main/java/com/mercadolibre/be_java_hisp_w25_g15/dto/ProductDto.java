package com.mercadolibre.be_java_hisp_w25_g15.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductDto(
        @Positive(message = "El product_id debe ser un número entero positivo") @NotNull(message = "El product_id no puede ser nulo") int product_id,
        @NotNull(message = "El product_name no puede ser nulo") String product_name,
        @NotNull(message = "El type no puede ser nulo") String type,
        @NotNull(message = "La marca no puede ser nula") String brand,
        @NotNull(message = "El color no puede ser nulo") String color,
        @NotNull(message = "Las notas no pueden ser nulas") String notes
) {
}
