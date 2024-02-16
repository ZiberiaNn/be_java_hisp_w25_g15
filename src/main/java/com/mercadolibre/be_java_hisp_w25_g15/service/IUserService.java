package com.mercadolibre.be_java_hisp_w25_g15.service;

import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;

public interface IUserService {
    MessageResponseDto unfollowSeller(int userId, int userIdToUnfollow);

    MessageResponseDto followSeller(int userId, int userIdToFollow);

    CountFollowersDto countFollowersByUserId(int userId);
}
