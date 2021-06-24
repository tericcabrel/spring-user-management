package com.tericcabrel.authorization.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tericcabrel.authorization.BaseIT;
import com.tericcabrel.authorization.models.dtos.CreateUserDto;
import com.tericcabrel.authorization.models.entities.Coordinates;
import com.tericcabrel.authorization.models.entities.User;
import com.tericcabrel.authorization.models.response.InvalidDataResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

public class RegisterUserIT extends BaseIT {
  private static final String ENDPOINT = "/auth/register";

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private JavaMailSender mailSender;

  @Captor
  ArgumentCaptor<MimeMessage> mimeMessageCaptor;

  @DisplayName("Register - Fail: Invalid data")
  @Test
  void testFailToRegisterUserCauseInvalidData() {
    HttpEntity<CreateUserDto> request = new HttpEntity<>(new CreateUserDto(), testUtility.createHeaders());

    ResponseEntity<InvalidDataResponse> response = restTemplate.postForEntity(ENDPOINT, request, InvalidDataResponse.class);

    assertThat(response.getStatusCodeValue()).isEqualTo(422);

    Map<String, Map<String, List<String>>> data = Objects.requireNonNull(response.getBody()).getData();

    assertThat(data.containsKey("errors")).isTrue();

    Map<String, List<String>> errors = data.get("errors");

    // errors.keySet().stream().forEach(System.out::println);

    assertThat(errors.containsKey("email")).isTrue();
    assertThat(errors.containsKey("lastName")).isTrue();
    assertThat(errors.containsKey("firstName")).isTrue();
    assertThat(errors.containsKey("confirmPassword")).isTrue();
    assertThat(errors.containsKey("timezone")).isTrue();
  }

  @DisplayName("Register - Register user successfully")
  @Test
  public void testRegisterUserSuccess() {
    // GIVEN
    CreateUserDto createUserDto = new CreateUserDto()
        .setFirstName("Teco")
        .setLastName("Gill")
        .setEmail("tecogill@email.com")
        .setPassword("password")
        .setConfirmPassword("password")
        .setCoordinates(new Coordinates(4.4545f, 9.73248f))
        .setGender("M")
        .setTimezone("Europe/Paris");

    HttpEntity<CreateUserDto> request = new HttpEntity<>(createUserDto, testUtility.createHeaders());

    /*Object registerResponse = restTemplate.postForEntity(ENDPOINT, request, Object.class);
    System.out.println(registerResponse);*/

    // WHEN
    when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

    doNothing().when(mailSender).send(mimeMessageCaptor.capture());

    ResponseEntity<User> registerResponse = restTemplate.postForEntity(ENDPOINT, request, User.class);

    System.out.println(registerResponse);

    // THEN
    assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    verify(mailSender, times(1)).send(mimeMessageCaptor.capture());

    User user = Objects.requireNonNull(registerResponse.getBody());

    try {
      Address[] addresses = mimeMessageCaptor.getValue().getAllRecipients();

      assertThat(addresses).hasSize(1);
      assertThat(addresses[0].toString()).isEqualTo(createUserDto.getEmail());
    } catch (MessagingException e) {
      e.printStackTrace();
    }

    assertThat(user.getEmail()).isEqualTo(createUserDto.getEmail());
    assertThat(user.getId()).isNotNull();
  }
}
