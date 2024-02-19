package com.mercadolibre.be_java_hisp_w25_g15.controller;

import com.mercadolibre.be_java_hisp_w25_g15.dto.request.UnfollowDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<MessageResponseDto> unfollowUser(@PathVariable int userId, @PathVariable int userIdToUnfollow){
        UnfollowDto unfollowDto = new UnfollowDto(userId, userIdToUnfollow);
        return new ResponseEntity<>(userService.unfollowSeller(unfollowDto), HttpStatus.OK);
    }

    /*@PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<MessageResponseDto> unfollowUser(@Valid @RequestBody UnfollowDto unfollowDto){
        return new ResponseEntity<>(userService.unfollowSeller(unfollowDto), HttpStatus.OK);
    }*/

}
