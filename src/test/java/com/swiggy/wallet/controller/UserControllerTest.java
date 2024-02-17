package com.swiggy.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.wallet.model.User;
import com.swiggy.wallet.requestmodels.UserRegisterRequest;
import com.swiggy.wallet.responsemodels.UserRegisterResponse;
import com.swiggy.wallet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @InjectMocks
    UserController controller;

    @Mock
    UserService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


//    @Test
//    void testExpectUserCreated() throws Exception {
//        UserRegisterRequest userRequestModel = new UserRegisterRequest("testUser", "testPassword");
//        UserRegisterResponse response = new UserRegisterResponse();
//        when(service.createNewUser(userRequestModel)).thenReturn(ResponseEntity.ok(response));
//
//        mockMvc.perform(post("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userRequestModel)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.userName").value("testUser"));
//        verify(service, times(1)).createNewUser(userRequestModel);
//    }
}