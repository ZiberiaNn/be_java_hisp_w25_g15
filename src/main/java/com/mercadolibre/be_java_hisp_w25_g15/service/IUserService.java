package com.mercadolibre.be_java_hisp_w25_g15.service;

import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;

import java.util.List;

public interface IUserService {
    MessageResponseDto unfollowSeller(int userId, int userIdToUnfollow);

    MessageResponseDto followSeller(int userId, int userIdToFollow);

    CountFollowersDto countFollowersByUserId(int userId);

    List<UserDto> listFollowersBySellerId(int sellerId);

    UserDto findAllFollwedByUser(int userId);

    List<UserDto> findAll();
}
