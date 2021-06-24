package com.tericcabrel.authorization.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tericcabrel.authorization.exceptions.GlobalExceptionHandler;
import com.tericcabrel.authorization.services.interfaces.RoleService;
import com.tericcabrel.authorization.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class RegisterUserTest {

  @Mock
  UserService userService;

  @Mock
  RoleService roleService;

  @InjectMocks
  AuthController authController;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(authController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  public void testFailToRegisterUserCauseInvalidData () throws Exception {
    MvcResult result = mockMvc.perform(
        post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}")
    )
        //.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        //.andExpect(content().json("{ \"n\": \"n\" }", true))
        // .andExpect(status().isUnprocessableEntity())
        .andExpect(status().isUnprocessableEntity())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data", hasKey("errors")))
        .andExpect(jsonPath("$.data.errors", hasKey("firstName")))
        .andExpect(jsonPath("$.data.errors", hasKey("lastName")))
        .andExpect(jsonPath("$.data.errors", hasKey("timezone")))
        .andExpect(jsonPath("$.data.errors", hasKey("confirmPassword")))
        .andExpect(jsonPath("$.data.errors", hasKey("email")))
        .andReturn();

    System.out.println(result.getResponse().getContentAsString());
  }


}
