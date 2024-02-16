package com.mercadolibre.be_java_hisp_w25_g15.dto;

import java.time.LocalDate;

public record PostDto (
        int userId,
        LocalDate date,
        ProductDto product,
        int category,
        double price
){
}
