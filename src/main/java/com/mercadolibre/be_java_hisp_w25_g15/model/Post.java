package com.mercadolibre.be_java_hisp_w25_g15.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
    int id;
    User user;
    LocalDate date;
    Product product;
    int category;
    double price;
    public Post(User user, LocalDate date, Product product, int category, double price) {
        this.id = idGenerator.incrementAndGet();
        this.user = user;
        this.date = date;
        this.product = product;
        this.category = category;
        this.price = price;
    }
}
