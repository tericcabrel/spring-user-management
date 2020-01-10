package com.tericcabrel.authorization.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

import com.tericcabrel.authorization.events.OnRegistrationCompleteEvent;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.services.interfaces.AccountConfirmationService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private Environment environment;

    private AccountConfirmationService accountConfirmationService;

    private JavaMailSender mailSender;

    public RegistrationListener(
            AccountConfirmationService accountConfirmationService,
            JavaMailSender mailSender,
            Environment environment
    ) {
        this.accountConfirmationService = accountConfirmationService;
        this.mailSender = mailSender;
        this.environment = environment;
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
        String confirmationUrl = environment.getProperty("app.url.confirm-account") + "?token=" + token;
        String message = "Welcome on identity!";

        // System.out.println("Name => " + environment.getProperty("mail.from.name"));
        // System.out.println("URL  => " + confirmationUrl);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setFrom(environment.getProperty("mail.from.name", "Identity"));
        email.setText(message + " \\r\\n" + confirmationUrl);

        mailSender.send(email);
    }
}