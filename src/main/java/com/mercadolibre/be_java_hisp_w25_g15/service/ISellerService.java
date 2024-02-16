package com.mercadolibre.be_java_hisp_w25_g15.service;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;

public interface ISellerService {
    CountFollowersDto numberOfFollowers(int id);
}
