package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.mercadolibre.be_java_hisp_w25_g15.dto.request.FollowDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.request.UnfollowDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;
import com.mercadolibre.be_java_hisp_w25_g15.exception.NotFoundException;
import com.mercadolibre.be_java_hisp_w25_g15.model.Buyer;
import com.mercadolibre.be_java_hisp_w25_g15.model.Seller;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.impl.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

     Optional<User> seller;
     Optional<User> sellerWithFollower;
     Optional<User> buyer;

    @BeforeEach
    void setUp(){
        buyer =  Optional.of(Buyer.builder().id(1).username("Luca").followed(new ArrayList<>()).build());
        seller = Optional.of(Seller.builder().id(2).username("Vendedor1").followed(new ArrayList<>()).followers(new ArrayList<>()).build());
        sellerWithFollower = Optional.of(Seller.builder().id(3).username("Vendedor con seguidores").followed(new ArrayList<>()).followers(new ArrayList<>(
                List.of(buyer.get())
        )).build());
        buyer.get().getFollowed().add(sellerWithFollower.get());
    }


    @Test
    void unfollowSellerOK() {
        //Arrange
        Integer userId = 1;
        Integer userIdToUnfollow = 3;
        UnfollowDto unfollowDtoParam = new UnfollowDto(userId, userIdToUnfollow);
        MessageResponseDto messageResponseDtoExpected = new MessageResponseDto("User unfollowed successfully");
        when(userRepository.getUserById(userId)).thenReturn(buyer);
        when(userRepository.getUserById(userIdToUnfollow)).thenReturn(sellerWithFollower);

        //Act
        MessageResponseDto messageResponseDto = this.userService.unfollowSeller(unfollowDtoParam);

        //Assert
        assertEquals(0, buyer.get().getFollowed().size());
        assertEquals(messageResponseDtoExpected.message(), messageResponseDto.message());
        verify(userRepository).updateFollowerList(seller.get());
        verify(userRepository).updateFollowedList(buyer.get());
    }

    @Test
    void unfollowNotExistingSellerOK() {
        //Arrange
        Integer userId = 1;
        Integer userIdToFollow = 2;
        UnfollowDto unfollowDto = new UnfollowDto(userId, userIdToFollow);
        Optional<User> buyerEmpty = Optional.empty();

        when(userRepository.getUserById(userId)).thenReturn(buyerEmpty);
        //Act & Assert
        Assertions.assertThrows(NotFoundException.class,
                () -> userService.unfollowSeller(unfollowDto));
    }

    @Test
    void followSellerOK() {
        //Arrange
        Integer userId = 1;
        Integer userIdToFollow = 2;
        FollowDto followDtoParam = new FollowDto(userId, userIdToFollow);
        MessageResponseDto messageResponseDtoExpected = new MessageResponseDto("Seller followed correctly");
        when(userRepository.getUserById(userId)).thenReturn(buyer);
        when(userRepository.getUserById(userIdToFollow)).thenReturn(seller);

        //Act
        MessageResponseDto messageResponseDto = this.userService.followSeller(followDtoParam);

        //Assert
        assertEquals(messageResponseDtoExpected.message(), messageResponseDto.message());
        verify(userRepository).updateFollowerList(seller.get());
        verify(userRepository).updateFollowedList(buyer.get());
    }
    @Test
    void followNotExistingSeller(){
        //Arrange
        Integer userId = 1;
        Integer userIdToFollow = 3;
        FollowDto followDto = new FollowDto(userId, userIdToFollow);
        Optional<User> buyerEmpty = Optional.empty();

        when(userRepository.getUserById(userId)).thenReturn(buyerEmpty);
        //Assert & Act
        Assertions.assertThrows(NotFoundException.class,
                () -> userService.followSeller(followDto));
    }

    @Test
    void countFollowersByUserId() {
        //Arrange
        Integer sellerId = 3;
        String sellerName = "Vendedor con seguidores";
        Integer countExpected = 1;
        CountFollowersDto countFollowersDtoExpected = new CountFollowersDto(sellerId, sellerName, countExpected);
        when(userRepository.getUserById(3)).thenReturn(sellerWithFollower);

        //Act
        CountFollowersDto countFollowersDtoResponse = this.userService.countFollowersByUserId(sellerId);
        //Assert
        Assertions.assertEquals(countFollowersDtoExpected, countFollowersDtoResponse);
    }

    @Test
    void findAllSellerFollowers() {
    }

    @Test
    void findAllFollowedByUser() {
    }

    @Test
    void findAll() {
    }
}