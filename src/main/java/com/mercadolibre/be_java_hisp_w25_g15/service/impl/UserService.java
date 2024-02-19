package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.mercadolibre.be_java_hisp_w25_g15.dto.request.UnfollowDto;
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
import com.mercadolibre.be_java_hisp_w25_g15.utils.ObjectMapperBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final ObjectMapperBean objectMapper;
    private final IUserRepository userRepository;

    @Override
    public MessageResponseDto unfollowSeller(UnfollowDto unfollowDto) {
        if (unfollowDto.userId() == unfollowDto.unfollowUserId()){
            throw new ConflictException("Users must be different");
        }
        Optional<User> buyer = userRepository.getUserById(unfollowDto.userId());
        if (buyer.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Optional<User> seller = userRepository.getUserById(unfollowDto.unfollowUserId());
        if (seller.isEmpty()){
            throw new NotFoundException("Seller not found");
        }
        if (buyer.get().getFollowed().stream().noneMatch(u -> u.getId() == seller.get().getId())) {
            throw new NotFoundException("Seller is not followed");
        }

        buyer.get().getFollowed().removeIf(followed -> followed.getId() == seller.get().getId());
        ((Seller) seller.get()).getFollowers().removeIf(follower -> follower.getId() == buyer.get().getId());
        userRepository.unfollowSeller(buyer.get(), seller.get());

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
            return new UserDto( user.getId(), user.getUsername(), parseUsersToUserListDto(((Seller) user).getFollowers(), order), null);
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
            return new UserDto( user.getId(), user.getUsername(), null, parseUsersToUserListDto(user.getFollowed(), order));
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
    private List<UserListDto> parseUsersToUserListDto(List<User> users , String order){
        Stream<UserListDto> userListDtoStream = users.stream()
                .map(user -> new UserListDto(user.getId(),user.getUsername()));
        if (order != null) {
            if (order.equals("name_asc")) {
                userListDtoStream = userListDtoStream.sorted(Comparator.comparing(UserListDto::getUsername));
            } else if (order.equals("name_desc")) {
                userListDtoStream = userListDtoStream.sorted(Comparator.comparing(UserListDto::getUsername).reversed());
            }
        }
        return userListDtoStream.toList();
    }

    // Método para convertir una lista Entidad tipo User a una lista Dto tipo UserDto
    private List<UserDto> parseUsersDto(List<User> users){
        return users.stream().map(users_->objectMapper.getMapper().convertValue(users_,UserDto.class))
                .collect(Collectors.toList());
    }

}
