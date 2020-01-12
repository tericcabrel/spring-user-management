package com.tericcabrel.authorization.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender sender;

    // @Value("${com.tec.flightreservation.itinerary.email.subject}")
    private String EMAIL_SUBJECT;

    // @Value("${com.tec.flightreservation.itinerary.email.body}")
    private String EMAIL_BODY;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

    public void sendItinerary(String address, String filePath) {
        LOGGER.info("Inside sendItinerary()");
        MimeMessage message = sender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setTo(address);
            messageHelper.setSubject(EMAIL_SUBJECT);
            messageHelper.setText(EMAIL_BODY);
            messageHelper.addAttachment("Itinerary", new File(filePath));

            sender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("Exception inside sendItinerary" + e);
            e.printStackTrace();
        }
    }
}
