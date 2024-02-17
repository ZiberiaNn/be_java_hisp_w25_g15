package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;
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

    @Override
    public UserDto findAllFollwedByUser(int userId) {
        /*
           1. Verificar si existe el usuario --> Si es null devolver una excepción afirmando que "no existe el usuario."
           2. Si existe el usuario pero no tiene seguidores --> (null o []) devolver una excepción afirmando que " el usuario no tiene seguidores."
           3. Si tiene seguidores entonces traer la data del repositorio y encapcularla en el objeto UserDto.
           (Tener en cuenta que el UserDto tiene una lista de tipo SellerDto)
         */
        return null;
    }
}
