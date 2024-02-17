package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.SellerDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;
import com.mercadolibre.be_java_hisp_w25_g15.exception.NotFoundException;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IUserRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    public UserService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
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
        // Se valida si el usuario existe
        if(userRepository.getUserById(userId).isEmpty()){
            throw  new NotFoundException("No existe el usuario");
        }
        // Se valida si el usuario tiene seguidores
        User user = userRepository.getFollowedUserById(userId);
        if(user == null){
            throw new NotFoundException("El usuario no tiene seguidores");
        }else{
            // Se encapsula en un objeto DTO con atributos DTO
            UserDto userDto = objectMapper.convertValue(user,UserDto.class);
            List<SellerDto> sellerDtos = parseSellersDto(user.getFollowed());
            return new UserDto(userDto.id(),userDto.username(),sellerDtos);
        }
    }

    @Override
    public List<UserDto> findAll() {
        if(userRepository.getAllUsers().isEmpty()){
            throw new NotFoundException("Usuarios no registrados");
        }
       return parseUsersDto(userRepository.getAllUsers());
    }

    // Método para convertir una lista Entidad tipo User a una lista Dto tipo SellerDto
    private List<SellerDto> parseSellersDto(List<User> sellers){
        return sellers.stream().map(seller->objectMapper.convertValue(seller,SellerDto.class))
                .collect(Collectors.toList());
    }

    // Método para convertir una lista Entidad tipo User a una lista Dto tipo UserDto
    private List<UserDto> parseUsersDto(List<User> users){
        return users.stream().map(users_->objectMapper.convertValue(users_,UserDto.class))
                .collect(Collectors.toList());
    }
}
