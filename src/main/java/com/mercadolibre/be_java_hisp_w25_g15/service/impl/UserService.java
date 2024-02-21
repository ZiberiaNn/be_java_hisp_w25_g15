package com.mercadolibre.be_java_hisp_w25_g15.service.impl;

import com.mercadolibre.be_java_hisp_w25_g15.dto.PostDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.ProductDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.request.UnfollowDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.*;
import com.mercadolibre.be_java_hisp_w25_g15.exception.ConflictException;
import com.mercadolibre.be_java_hisp_w25_g15.exception.NotFoundException;
import com.mercadolibre.be_java_hisp_w25_g15.model.Post;
import com.mercadolibre.be_java_hisp_w25_g15.model.Product;
import com.mercadolibre.be_java_hisp_w25_g15.model.Seller;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import com.mercadolibre.be_java_hisp_w25_g15.repository.IUserRepository;
import com.mercadolibre.be_java_hisp_w25_g15.service.IUserService;
import com.mercadolibre.be_java_hisp_w25_g15.utils.ObjectMapperBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
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
        userRepository.updateFollowedList(buyer.get());
        userRepository.updateFollowerList(seller.get());
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

        user.get().getFollowed().add(userToFollow.get());
        ((Seller) userToFollow.get()).getFollowers().add(user.get());

        this.userRepository.updateFollowerList(userToFollow.get());
        this.userRepository.updateFollowedList(user.get());

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
        Optional<User> user = userRepository.getUserById(userId);
        // Se valida si el usuario existe
        if(user.isEmpty()){
            throw  new NotFoundException("User not found");
        }
        if(user.get().getFollowed().isEmpty()){
            throw new NotFoundException("User has not followed");
        }else{
            // Se encapsula en un objeto DTO con atributos DTO
            return new UserDto( user.get().getId(), user.get().getUsername(), null, sortUserListDto(createUserListDto(user.get().getFollowed()), order));
        }
    }

    @Override
    public List<UserListDto> findAll() {
        if(userRepository.getAllUsers().isEmpty()){
            throw new NotFoundException("Usuarios no registrados");
        }
       return parseUsersDto(userRepository.getAllUsers());
    }

    @Override
    public List<UserListDto> findAllPage(int page, int size) {
        if(userRepository.getAllUsers().isEmpty()){
            throw new NotFoundException("Users don't registered");
        }
        int startIndex = page * size;
        int endIndex = Math.min(startIndex +  size, userRepository.getAllUsers().size());
        if(startIndex>=endIndex){
            throw new NotFoundException("There aren't users in this range");
        }
        return parseUsersDto(userRepository.getAllUsers().subList(startIndex,endIndex));
    }

    @Override
    public PostGetListDto findAllProductsPromoByUser(int userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if(user.isEmpty()){
            throw  new NotFoundException("User not found");
        }
        if(user.get().getPosts().isEmpty()){
            throw new NotFoundException("User "+ user.get().getId() +" has not posts");
        }else{
            List<Post> posts = user.get().getPosts().stream().filter(post->post.getUserId()==userId && post.isHas_promo()).toList();
            if(posts.isEmpty()){
                throw new NotFoundException("User " + user.get().getUsername() + " has not promo products");
            }
            return new PostGetListDto( user.get().getId(), user.get().getUsername(), parsePostsDto(posts));
        }
    }

    @Override
    public CountPromoProductsDto countAllPromoProductsByUser(int userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if(user.isEmpty()){
            throw  new NotFoundException("User not found");
        }
        if(user.get().getPosts().isEmpty()){
            throw new NotFoundException("User "+ user.get().getUsername() +" has not posts");
        }else{
            long countPosts = user.get().getPosts().stream().filter(post->post.getUserId()==userId && post.isHas_promo()).count();
            if(countPosts == 0){
                throw new NotFoundException("User " + user.get().getUsername() + " has not promo products");
            }
            return new CountPromoProductsDto( user.get().getId(), user.get().getUsername(),(int)countPosts);
        }
    }

    @Override
    public PostGetListDto findAllProductsNotPromoByUser(int userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if(user.isEmpty()){
            throw  new NotFoundException("User not found");
        }
        if(user.get().getPosts().isEmpty()){
            throw new NotFoundException("User "+ user.get().getId() +" has not posts");
        }else{
            List<Post> posts = user.get().getPosts().stream().filter(post->post.getUserId()==userId && !post.isHas_promo()).toList();
            if(posts.isEmpty()){
                throw new NotFoundException("User " + user.get().getUsername() + " has not products without promo");
            }
            return new PostGetListDto( user.get().getId(), user.get().getUsername(), parsePostsDto(posts));
        }
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
    private List<UserListDto> parseUsersDto(List<User> users){
        return users.stream().map(u -> new UserListDto(u.getId(), u.getUsername()))
                .toList();
    }

    private ProductDto parseProductDto(Product product){
        return new ProductDto(product.getId(),product.getName(),product.getType(),product.getBrand(),product.getColor(),product.getNotes());
    }

    private List<PostDto> parsePostsDto(List<Post> posts){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return posts.stream().map(p -> new PostDto(p.getUserId(),p.getId(),p.getDate().format(formatter),parseProductDto(p.getProduct()),p.getCategory(),p.getPrice(),p.isHas_promo(),p.getDiscount()))
                .toList();
    }
}
