package com.tericcabrel.authorization.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.tericcabrel.authorization.BaseIT;
import com.tericcabrel.authorization.models.dtos.CreateUserDto;
import com.tericcabrel.authorization.models.entities.Coordinates;
import com.tericcabrel.authorization.models.response.UserResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthControllerIT extends BaseIT {
  TestRestTemplate restTemplate;

  void testRegisterUser() {
    // GIVEN
    CreateUserDto createUserDto = new CreateUserDto()
        .setFirstName("Teco")
        .setLastName("Gill")
        .setEmail("tecogill@email.com")
        .setPassword("password")
        .setConfirmPassword("password")
        .setCoordinates(new Coordinates(4.4545f, 9.73248f))
        .setGender("M");

    // WHEN
    ResponseEntity<UserResponse> userResponse = restTemplate.postForEntity("/auth/register", createUserDto, UserResponse.class);

    // THEN
    assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
