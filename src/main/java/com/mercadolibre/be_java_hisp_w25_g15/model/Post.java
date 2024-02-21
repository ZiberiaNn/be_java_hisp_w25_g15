package com.mercadolibre.be_java_hisp_w25_g15.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    static final AtomicInteger idGenerator = new AtomicInteger();
    @JsonProperty("post_id")
    int id;
    @JsonProperty("user_id")
    int userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate date;
    Product product;
    int category;
    double price;
    boolean has_promo;
    @Nullable
    double discount;
    public Post(int user, LocalDate date, Product product, int category, double price, boolean has_promo, double discount) {
        this.id = idGenerator.incrementAndGet();
        this.userId = user;
        this.date = date;
        this.product = product;
        this.category = category;
        this.price = price;
        this.has_promo = has_promo;
        this.discount = discount;
    }
    public void setPostIdAuto(){
        this.id = idGenerator.incrementAndGet();
    }
}
