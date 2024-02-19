package com.mercadolibre.be_java_hisp_w25_g15.service;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;

import java.util.List;

public interface ISellerService {
    CountFollowersDto numberOfFollowers(int id);
    List<UserDto> findAllFollowersByUser(int sellerId);

}
