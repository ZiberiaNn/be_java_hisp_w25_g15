package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.SellerDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;
import com.mercadolibre.be_java_hisp_w25_g15.exception.ConflictException;
import com.mercadolibre.be_java_hisp_w25_g15.exception.NotFoundException;
import com.mercadolibre.be_java_hisp_w25_g15.model.Seller;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IUserRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;
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
        Optional<User> user = this.userRepository.getUserById(userId);
        if(user.isEmpty())
            throw new NotFoundException("User not found");
        Optional<User> userToFollow = this.userRepository.getUserById(userIdToFollow);
        if(userToFollow.isEmpty())
            throw new NotFoundException("User to follow not found");
        if(!(userToFollow.get() instanceof Seller))
            throw new ConflictException("User to follow is not a Seller");
        Optional<User> resultSearchUserInFollowersOfSeller = ((Seller) userToFollow.get())
                .getFollowers()
                .stream()
                .filter((v) -> v.getId() == userId)
                .findFirst();
        if(resultSearchUserInFollowersOfSeller.isPresent())
            throw new ConflictException("User already is following");
        this.userRepository.followSeller(userId, userIdToFollow);
        return new MessageResponseDto("Seller followed correctly");
    }

    @Override
    public CountFollowersDto countFollowersByUserId(int userId){
        return null;
    }
    @Override
    public UserDto findAllSellerFollowers(int sellerId){
        if (this.userRepository.getUserById(sellerId).isEmpty()) {
            throw new NotFoundException("Seller not found");
        }else if (!(this.userRepository.getUserById(sellerId).get() instanceof Seller)) {
            throw new NotFoundException("User is not a seller");
        } else if (this.userRepository.getUserById(sellerId).get().getFollowed().isEmpty()) {
            throw new NotFoundException("Seller has no followers");
        }else {
            User user = userRepository.getFollowedUserById(sellerId);
            UserDto userDto = objectMapper.convertValue(user,UserDto.class);
            List<SellerDto> sellerDtos = parseSellersDto(user.getFollowed());
            return new UserDto(userDto.id(),userDto.username(),sellerDtos);
        }
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
