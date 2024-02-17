package com.mercadolibre.be_java_hisp_w25_g15.repository;

import com.mercadolibre.be_java_hisp_w25_g15.model.User;

import java.util.Optional;

public interface IUserRepository {
    void followSeller(int userId, int userIdToFollow);
    void unfollowSeller(int userId, int userIdToFollow);
    Optional<User> getUserById(int userId);
    Optional<User> getFollowedUserById(int userId);
}
