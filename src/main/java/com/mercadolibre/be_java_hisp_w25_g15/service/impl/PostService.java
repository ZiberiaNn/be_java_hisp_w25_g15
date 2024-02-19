package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mercadolibre.be_java_hisp_w25_g15.dto.PostDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.PostGetListDto;
import com.mercadolibre.be_java_hisp_w25_g15.exception.NotFoundException;
import com.mercadolibre.be_java_hisp_w25_g15.model.Post;
import com.mercadolibre.be_java_hisp_w25_g15.model.Seller;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IPostRepository;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IUserRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.IPostService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {
    private IPostRepository postRepository;
    private IUserRepository userRepository;

    public PostService(IPostRepository postRepository, IUserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);

        Post post = objectMapper.convertValue(postDto, Post.class);
        Optional<User> user = userRepository.getUserById(postDto.user_id());
        if (user.isPresent()) {
           //si el usuario es Seller puedo publicar
            if((user.get() instanceof Seller)){
                post.setUserId(user.get().getId());
            }else {
                throw new ConflictException("User must be a seller to create a post");
            }
            post.setUserId(user.get().getId());
        }else{
            throw new NotFoundException("User not found");
        }

        Post newPost = postRepository.addPost(post);

        return objectMapper.convertValue(newPost, PostDto.class);

    }

    @Override
    public PostGetListDto getPostsBySellerIdLastTwoWeeks(int sellerId) {
        return null;
    }


}
