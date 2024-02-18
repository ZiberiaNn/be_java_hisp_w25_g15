package com.mercadolibre.be_java_hisp_w25_g15.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record PostDto (
        int user_id,
        @JsonFormat(pattern = "dd-MM-yyyy") LocalDate date,
        ProductDto product,
        int category,
        double price
){
}
