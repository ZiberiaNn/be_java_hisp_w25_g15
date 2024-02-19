package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.mercadolibre.be_java_hisp_w25_g15.dto.request.UnfollowDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.CountFollowersDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.MessageResponseDto;
import com.mercadolibre.be_java_hisp_w25_g15.exception.NotFoundException;
import com.mercadolibre.be_java_hisp_w25_g15.model.Seller;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IUserRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    private IUserRepository userRepository;

    public UserService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public MessageResponseDto unfollowSeller(UnfollowDto unfollowDto) {
        Optional<User> buyer = userRepository.getUserById(unfollowDto.userId());
        Optional<User> seller = userRepository.getUserById(unfollowDto.unfollowUserId());
        if (buyer.isEmpty() || seller.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        if (!(seller.get() instanceof Seller)) {
            throw new IllegalArgumentException("User to follow is not a Seller");
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
        return null;
    }

    @Override
    public CountFollowersDto countFollowersByUserId(int userId){
        return null;
    }
}
