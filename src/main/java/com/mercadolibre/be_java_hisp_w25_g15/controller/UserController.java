package com.mercadolibre.be_java_hisp_w25_g15.controller;

import com.mercadolibre.be_java_hisp_w25_g15.dto.request.UnfollowDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.*;
import com.mercadolibre.be_java_hisp_w25_g15.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<MessageResponseDto> followUser(
            @PathVariable int userId,
            @PathVariable int userIdToFollow
    ) {
        return new ResponseEntity<>(
                this.userService.followSeller(userId, userIdToFollow),
                HttpStatus.OK
        );
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<CountFollowersDto> countFollowers(
            @PathVariable int userId
    ) {
        return new ResponseEntity<>(
                this.userService.countFollowersByUserId(userId),
                HttpStatus.OK
        );
    }


    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<MessageResponseDto> unfollowUser(@PathVariable int userId, @PathVariable int userIdToUnfollow){
        UnfollowDto unfollowDto = new UnfollowDto(userId, userIdToUnfollow);
        return new ResponseEntity<>(userService.unfollowSeller(unfollowDto), HttpStatus.OK);
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<UserListDto>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/get-users-page")
    public ResponseEntity<List<UserListDto>> getAllUsersPage(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size) {
        return new ResponseEntity<>(userService.findAllPage(page, size), HttpStatus.OK);
    }

    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<UserDto> getAllFollowedByUser(@PathVariable int userId, @RequestParam(name = "order", required = false) String order) {
        return new ResponseEntity<>(userService.findAllFollowedByUser(userId, order), HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<UserDto> getAllFollowersByUser(@PathVariable int userId, @RequestParam(name = "order", required = false) String order) {
        return new ResponseEntity<>(userService.findAllSellerFollowers(userId, order), HttpStatus.OK);
    }
    @GetMapping("product/promo-post/list")
    public ResponseEntity<PostGetListDto> getAllProductsPromoByUser(@RequestParam int user_id){
        return new ResponseEntity<>(userService.findAllProductsPromoByUser(user_id), HttpStatus.OK);
    }

    @GetMapping("product/promo-post/count")
    public ResponseEntity<CountPromoProductsDto> countAllProductsPromoByUser (@RequestParam int user_id){
        return new ResponseEntity<>(userService.countAllPromoProductsByUser(user_id), HttpStatus.OK);
    }

    @GetMapping("product/not-promo-post/list")
    public ResponseEntity<PostGetListDto> getAllProductsNotPromoByUser(@RequestParam int user_id){
        return new ResponseEntity<>(userService.findAllProductsNotPromoByUser(user_id), HttpStatus.OK);
    }
}
