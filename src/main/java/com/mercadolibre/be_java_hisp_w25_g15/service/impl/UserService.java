package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IUserRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    private IUserRepository userRepository;

    public UserService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public MessageResponseDto unfollowSeller(int userId, int userIdToUnfollow){
        return null;
    }

    @Override
    public MessageResponseDto followSeller(int userId, int userIdToFollow){
        return null;
    }

    @Override
    public CountFollowersDto countFollowersByUserId(int userId){
        return null;
    }
}
