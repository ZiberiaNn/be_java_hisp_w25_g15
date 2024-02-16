package com.mercadolibre.be_java_hisp_w25_g15.controller;

import com.mercadolibre.be_java_hisp_w25_g15.dto.PostDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.PostGetListDto;
import com.mercadolibre.be_java_hisp_w25_g15.service.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class PostController {
    private final IPostService iPostService;


    @PostMapping("/post")
    ResponseEntity<PostDto> createPost(@RequestBody PostDto post){
        return new ResponseEntity<>(iPostService.createPost(post), HttpStatus.OK);
    }
    @GetMapping("/followed/{userId}/list")
    ResponseEntity<PostGetListDto> getPostsBySellerIdLastTwoWeeks(@PathVariable(name = "userId") int userId){
        return new ResponseEntity<>(iPostService.getPostsBySellerIdLastTwoWeeks(userId), HttpStatus.OK);
    }
}
