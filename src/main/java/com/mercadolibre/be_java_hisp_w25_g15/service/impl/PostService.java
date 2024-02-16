package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.be_java_hisp_w25_g15.dto.PostDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.PostGetListDto;
import com.mercadolibre.be_java_hisp_w25_g15.model.Post;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IPostRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.IPostService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService implements IPostService {
    private IPostRepository postRepository;


    public PostService(IPostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto){
        //podemos validar si es una fecha previa a la actual
        ObjectMapper objectMapper = new ObjectMapper();
        Post post = objectMapper.convertValue(postDto, Post.class);
        Post newPost = postRepository.addPost(post);

        return objectMapper.convertValue(newPost, PostDto.class);

    }

    @Override
    public PostGetListDto getPostsBySellerIdLastTwoWeeks(int sellerId){
        return  null;
    }
}
