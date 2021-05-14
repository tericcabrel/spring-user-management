package com.tericcabrel.authorization.listeners;

import com.tericcabrel.authorization.services.interfaces.UserAccountService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.tericcabrel.authorization.models.entities.User;
import com.tericcabrel.authorization.events.OnResetPasswordEvent;


@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {
    private static final String TEMPLATE_NAME = "html/password-reset";
    private static final String MAIL_SUBJECT = "Password Reset";

    private final Environment environment;

    private final UserAccountService userAccountService;

    private final JavaMailSender mailSender;

    private final TemplateEngine htmlTemplateEngine;

    public ResetPasswordListener(
            UserAccountService userAccountService,
            JavaMailSender mailSender,
            Environment environment,
            TemplateEngine htmlTemplateEngine
    ) {
        this.userAccountService = userAccountService;
        this.mailSender = mailSender;
        this.environment = environment;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    @Override
    public void onApplicationEvent(OnResetPasswordEvent event) {
        this.sendResetPasswordEmail(event);
    }

    private void sendResetPasswordEmail(OnResetPasswordEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userAccountService.save(user, token);

        String resetUrl = environment.getProperty("app.url.password-reset") + "?token=" + token;
        String mailFrom = environment.getProperty("spring.mail.properties.mail.smtp.from");
        String mailFromName = environment.getProperty("mail.from.name", "Identity");

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        try {
            email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            email.setTo(user.getEmail());
            email.setSubject(MAIL_SUBJECT);
            email.setFrom(new InternetAddress(mailFrom, mailFromName));

            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("email", user.getEmail());
            ctx.setVariable("name", user.getFirstName() + " " + user.getLastName());
            ctx.setVariable("url", resetUrl);

            final String htmlContent = this.htmlTemplateEngine.process(TEMPLATE_NAME, ctx);

            email.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}