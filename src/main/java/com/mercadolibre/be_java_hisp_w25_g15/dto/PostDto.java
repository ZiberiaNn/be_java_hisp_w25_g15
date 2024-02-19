package com.mercadolibre.be_java_hisp_w25_g15.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record PostDto (
        @Positive(message = "El user_id debe ser un número entero positivo") int user_id,
        @NotNull(message = "La fecha no puede ser nula")

        @Pattern(regexp = "^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(19|20)\\d{2}|\\d{4}$", message = "El formato de la fecha debe ser dd-MM-yyyy")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        String date,
        @Valid @NotNull(message = "El producto no puede ser nulo")
        ProductDto product,
        @Positive(message = "La categoría debe ser un número entero positivo")
        int category,
        @Positive(message = "El precio debe ser un número positivo")
        double price
){
}
