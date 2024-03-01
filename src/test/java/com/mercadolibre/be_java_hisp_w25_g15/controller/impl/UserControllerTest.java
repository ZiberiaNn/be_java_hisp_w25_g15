package com.mercadolibre.be_java_hisp_w25_g15.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mercadolibre.be_java_hisp_w25_g15.dto.ErrorDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.PostDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.ProductDto;
import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserDto;

import com.mercadolibre.be_java_hisp_w25_g15.dto.response.UserListDto;
import com.mercadolibre.be_java_hisp_w25_g15.model.Buyer;
import com.mercadolibre.be_java_hisp_w25_g15.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void intAllUsersTest()throws Exception{
        ObjectWriter mapper = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer();

                List<UserListDto> users = List.of(
                        new UserListDto(1,"Tony Stark"),
                        new UserListDto(2,"Luca"),
                        new UserListDto(3,"Martin"),
                        new UserListDto(4,"Santiago"),
                        new UserListDto(5,"Orlando"),
                        new UserListDto(6,"Miguel"),
                        new UserListDto(7,"Samuel"),
                        new UserListDto(8,"Tony Stark")
                );

        String listJSON = mapper.writeValueAsString(users);

        MvcResult mvcResult = mockMvc.perform(get("/users/get-users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        assertEquals(listJSON, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void intCreatePostTest() throws Exception {

        ProductDto productDto1 = new ProductDto(1, "Leche","Lacteo", "Milkaut", "Blanco", "");
        PostDto postDto = new PostDto(1,5,"15-02-2024", productDto1, 1,15.0);

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer();

        String payloadDto = writer.writeValueAsString(postDto);

        MvcResult mvcResult = mockMvc.perform(post("/products/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDto))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(result -> assertFalse(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andReturn();

        assertEquals(payloadDto,mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void failedCreatePostTest()throws Exception{

        ProductDto productDto1 = new ProductDto(1, "Leche","Lacteo", "Milkaut", "Blanco", "");
        PostDto postDto = new PostDto(1,5,"15-02-2024", null, 1,15.0);

        ErrorDto errorDto = new ErrorDto("Post must be products associated");

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer();

        String payloadDto = writer.writeValueAsString(postDto);
        String errorExpected = writer.writeValueAsString(errorDto);

        MvcResult mvcResult= mockMvc.perform(post("/products/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDto))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertEquals(errorExpected,mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }
}
