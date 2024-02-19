package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.SellerDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;
import com.mercadolibre.be_java_hisp_w25_g15.exception.NotFoundException;
import com.mercadolibre.be_java_hisp_w25_g15.model.Seller;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IUserRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.ISellerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    private List<UserDto> parseUsersDto(List<User> users){
        ObjectMapper objectMapper = new ObjectMapper();
        return users.stream().map(users_->objectMapper.convertValue(users_,UserDto.class))
                .collect(Collectors.toList());
    }
}
