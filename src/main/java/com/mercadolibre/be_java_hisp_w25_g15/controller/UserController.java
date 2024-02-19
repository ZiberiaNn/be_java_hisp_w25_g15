package com.mercadolibre.be_java_hisp_w25_g15.controller;

import com.mercadolibre.be_java_hisp_w25_g15.dto.request.UnfollowDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;
import com.mercadolibre.be_java_hisp_w25_g15.service.IUserService;
import com.mercadolibre.be_java_hisp_w25_g15.service.impl.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mercadolibre.be_java_hisp_w25_g15.service.ISellerService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    IUserService userService;
    ISellerService sellerService;

    public UserController(IUserService userService, SellerService sellerService){
        this.userService = userService;
        this.sellerService = sellerService;
    }

    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<MessageResponseDto> unfollowUser(@RequestBody UnfollowDto unfollowDto){
        return  new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/followed/list")
    public ResponseEntity<UserDto> getAllFollowedByUser(@PathVariable int userId){
        return new ResponseEntity<>(userService.findAllFollwedByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/followers/list")
    public ResponseEntity<List<UserDto>> getAllFollowersByUser(@PathVariable int userId){
        return new ResponseEntity<>(sellerService.findAllFollowersByUser(userId), HttpStatus.OK);
    }
}
