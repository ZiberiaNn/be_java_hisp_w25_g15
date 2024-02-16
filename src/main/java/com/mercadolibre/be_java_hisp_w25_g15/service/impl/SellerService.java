package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IUserRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.ISellerService;
import org.springframework.stereotype.Service;

@Service
public class SellerService implements ISellerService {

    IUserRepository usersRepository;

    public SellerService(IUserRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @Override
    public CountFollowersDto numberOfFollowers(int id){

        return null;
    }
}
