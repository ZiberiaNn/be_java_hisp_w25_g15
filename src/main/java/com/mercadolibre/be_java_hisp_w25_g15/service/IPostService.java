package com.mercadolibre.be_java_hisp_w25_g15.service;
import com.mercadolibre.be_java_hisp_w25_g15.dto.PostDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.PostGetListDto;
import java.util.List;
public interface IPostService {
    PostDto createPost(PostDto postDto);

    PostGetListDto getPostsBySellerIdLastTwoWeeks(int sellerId);


}
