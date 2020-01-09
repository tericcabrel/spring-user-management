package com.tericcabrel.authorization.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

import com.tericcabrel.authorization.events.OnRegistrationCompleteEvent;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.services.interfaces.AccountConfirmationService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private AccountConfirmationService accountConfirmationService;

    private JavaMailSender mailSender;

    public RegistrationListener(AccountConfirmationService accountConfirmationService, JavaMailSender mailSender) {
        this.accountConfirmationService = accountConfirmationService;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        accountConfirmationService.save(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "/confirm-account?token=" + token;
        String message = "Welcome on identity!";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " rn" + "http://localhost:8080" + confirmationUrl);

        mailSender.send(email);
    }
}