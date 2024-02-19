package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.mercadolibre.be_java_hisp_w25_g15.dto.request.UnfollowDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserListDto;
import com.mercadolibre.be_java_hisp_w25_g15.exception.ConflictException;
import com.mercadolibre.be_java_hisp_w25_g15.exception.NotFoundException;
import com.mercadolibre.be_java_hisp_w25_g15.model.Seller;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IUserRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService implements IUserService {

    private final ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    public UserService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public MessageResponseDto unfollowSeller(UnfollowDto unfollowDto) {
        Optional<User> buyer = userRepository.getUserById(unfollowDto.userId());
        Optional<User> seller = userRepository.getUserById(unfollowDto.unfollowUserId());
        if (buyer.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        if (seller.isEmpty()){
            throw new NotFoundException("Seller not found");
        }
        if (buyer.get().getFollowed().stream().noneMatch(u -> u.getId() == seller.get().getId())) {
            throw new NotFoundException("Seller is not followed");
        }

        buyer.get().getFollowed().remove(seller.get());
        userRepository.unfollowSeller(buyer.get());

        return new MessageResponseDto("User unfollowed successfully");
    }

    @Override
    public MessageResponseDto followSeller(int userId, int userIdToFollow){
        if(userId == userIdToFollow)
            throw new ConflictException("Users must be different");
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
        Optional<User> user = this.userRepository.getUserById(userId);
        if(user.isEmpty())
            throw new NotFoundException("User not found");
        if(!(user.get() instanceof Seller))
            throw new ConflictException("User is not a Seller");
        return new CountFollowersDto(
          user.get().getId(),
          user.get().getUsername(),
          ((Seller) user.get()).getFollowers().size()
        );
    }
    @Override
    public UserDto findAllSellerFollowers(int sellerId, String order){
        Optional<User> optionalUser = this.userRepository.getUserById(sellerId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Seller not found");
        }

        User user = optionalUser.get();

        if (!(user instanceof Seller)) {
            throw new NotFoundException("User is not a seller");
        } else if (((Seller) user).getFollowers().isEmpty()) {
            throw new NotFoundException("Seller has no followers");
        } else {
            List<UserListDto> userListDtos = createUserListDto(((Seller) user).getFollowers());
            return new UserDto( user.getId(), user.getUsername(), sortUserListDto(userListDtos, order) , null);
        }
    }

    @Override
    public UserDto findAllFollowedByUser(int userId, String order) {
        // Se valida si el usuario existe
        if(userRepository.getUserById(userId).isEmpty()){
            throw  new NotFoundException("User not found");
        }
        // Se valida si el usuario tiene seguidores
        User user = userRepository.getFollowedUserById(userId);
        if(user == null){
            throw new NotFoundException("User has not followed");
        }else{
            // Se encapsula en un objeto DTO con atributos DTO
            List<UserListDto> userListDtos = createUserListDto(user.getFollowed());
            return new UserDto( user.getId(), user.getUsername(), null, sortUserListDto(userListDtos, order));
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
    private List<UserListDto> createUserListDto(List<User> users){
        return users.stream()
                .map(user -> new UserListDto(user.getId(),user.getUsername()))
                .toList();
    }

    private List<UserListDto> sortUserListDto(List<UserListDto> userListDtos, String order){
        if (order != null) {
            if (order.equals("name_asc") && userListDtos.size() > 1) {
                // sort by name asc
                userListDtos = userListDtos.stream().sorted(Comparator.comparing(UserListDto::getUsername)).collect(Collectors.toList());
            } else if (order.equals("name_desc") && userListDtos.size() > 1) {
                // sort by name desc
                userListDtos = userListDtos.stream().sorted(Comparator.comparing(UserListDto::getUsername).reversed()).collect(Collectors.toList());
            }
        }
        return userListDtos;
    }

    // Método para convertir una lista Entidad tipo User a una lista Dto tipo UserDto
    private List<UserDto> parseUsersDto(List<User> users){
        return users.stream().map(users_->objectMapper.convertValue(users_,UserDto.class))
                .collect(Collectors.toList());
    }
}
