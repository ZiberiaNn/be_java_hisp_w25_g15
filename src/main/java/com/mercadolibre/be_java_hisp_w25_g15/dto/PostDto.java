package com.mercadolibre.be_java_hisp_w25_g15.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record PostDto (
        @Positive(message = "El user_id debe ser un número entero positivo") int user_id,
        @NotNull(message = "La fecha no puede ser nula") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate date,
        @Valid @NotNull(message = "El producto no puede ser nulo")
        ProductDto product,
        @Positive(message = "La categoría debe ser un número entero positivo")
        int category,
        @Positive(message = "El precio debe ser un número positivo")
        double price
){
}
