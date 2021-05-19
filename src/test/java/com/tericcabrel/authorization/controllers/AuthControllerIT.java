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

public class AuthControllerIT extends BaseIT {
  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private JavaMailSender mailSender;

  @Captor
  ArgumentCaptor<MimeMessage> mimeMessageCaptor;

  @DisplayName("Register - Register user successfully")
  @Test
  public void testRegisterUser() {
    // GIVEN
    CreateUserDto createUserDto = new CreateUserDto()
        .setFirstName("Teco")
        .setLastName("Gill")
        .setEmail("tecogill@email.com")
        .setPassword("password")
        .setConfirmPassword("password")
        .setCoordinates(new Coordinates(4.4545f, 9.73248f))
        .setGender("M");

    HttpEntity<CreateUserDto> request = new HttpEntity<>(createUserDto, testUtility.createHeaders());

    // WHEN
    when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

    doNothing().when(mailSender).send(mimeMessageCaptor.capture());

    ResponseEntity<User> registerResponse = restTemplate.postForEntity("/auth/register", request, User.class);
    // Object userResponse = restTemplate.postForObject("/auth/register", request, Object.class);

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
