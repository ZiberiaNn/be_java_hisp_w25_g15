package com.mercadolibre.be_java_hisp_w25_g15.service;

import com.mercadolibre.be_java_hisp_w25_g15.dto.request.UnfollowDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.*;

import java.util.List;

public interface IUserService {
    MessageResponseDto unfollowSeller(UnfollowDto unfollowDto);

    MessageResponseDto followSeller(int userId, int userIdToFollow);

    CountFollowersDto countFollowersByUserId(int userId);

    UserDto findAllFollowedByUser(int userId, String order);

    UserDto findAllSellerFollowers(int sellerId, String order);

    List<UserListDto> findAll();

    List<UserListDto> findAllPage( int page, int size);
    PostGetListDto findAllProductsPromoByUser(int userId);
    CountPromoProductsDto countAllPromoProductsByUser(int userId);
    PostGetListDto findAllProductsNotPromoByUser(int userId);
}
