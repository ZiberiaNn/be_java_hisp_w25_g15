package com.mercadolibre.be_java_hisp_w25_g15.repository.impl;

import com.mercadolibre.be_java_hisp_w25_g15.model.Post;
import com.mercadolibre.be_java_hisp_w25_g15.model.Product;
import com.mercadolibre.be_java_hisp_w25_g15.model.Seller;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IPostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Repository
@RequiredArgsConstructor
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRepository implements IPostRepository {
    final List<Post> posts = new ArrayList<>(
            List.of(
                new Post(
                        Seller.builder().id(1).username("Tony Stark").followed(new ArrayList<>()).followers(new ArrayList<>()).build(),
                        LocalDate.of(2020, 5, 5),
                        new Product(1, "Mouse", "Electronico", "Logitech", "Negro", "N/A"),
                        1,
                        300.0
                )
            )
    );
    @Override
    public List<Post> findAllPostsBySellerIdBetweenDateRange(int sellerId, LocalDate startDate, LocalDate endDate){
        return posts.stream()
                .filter(post ->
                        post.getId() == sellerId
                        && post.getDate().isBefore(endDate)
                        && post.getDate().isAfter(startDate))
                .toList();
    }
    
    @Override
    public Post addPost(Post post){
        posts.add(post);
        return post;
    }
}
