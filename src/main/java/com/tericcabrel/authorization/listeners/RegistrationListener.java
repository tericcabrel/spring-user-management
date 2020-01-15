package com.tericcabrel.authorization.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.tericcabrel.authorization.events.OnRegistrationCompleteEvent;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.services.interfaces.ConfirmAccountService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private static final String TEMPLATE_NAME = "html/registration";
    private static final String SPRING_LOGO_IMAGE = "templates/html/images/spring.png";
    private static final String PNG_MIME = "image/png";
    private static final String MAIL_SUBJECT = "Registration Confirmation";

    private Environment environment;

    private ConfirmAccountService confirmAccountService;

    private JavaMailSender mailSender;

    private TemplateEngine htmlTemplateEngine;

    public RegistrationListener(
            ConfirmAccountService confirmAccountService,
            JavaMailSender mailSender,
            Environment environment,
            TemplateEngine htmlTemplateEngine
    ) {
        this.confirmAccountService = confirmAccountService;
        this.mailSender = mailSender;
        this.environment = environment;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        confirmAccountService.save(user, token);

        String confirmationUrl = environment.getProperty("app.url.confirm-account") + "?token=" + token;

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        try {
            email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            email.setTo(user.getEmail());
            email.setSubject(MAIL_SUBJECT);
            email.setFrom(environment.getProperty("mail.from.name", "Identity"));

            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("email", user.getEmail());
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("springLogo", SPRING_LOGO_IMAGE);
            ctx.setVariable("url", confirmationUrl);

            final String htmlContent = this.htmlTemplateEngine.process(TEMPLATE_NAME, ctx);

            email.setText(htmlContent, true);

            ClassPathResource clr = new ClassPathResource(SPRING_LOGO_IMAGE);

            email.addInline("springLogo", clr, PNG_MIME);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}