package com.stibits.rnft.common.helpers;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.stibits.rnft.common.utils.MailServiceConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailService {
    private Session mailSession;
    private MailServiceConfig config;

    public MailService (MailServiceConfig config) {
        Properties mailProperties = this.getMailProperties(config);
        String user = config.getUser();
        String password = config.getPassword();

        this.config = config;

        this.mailSession = Session.getInstance(mailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
    }

    public void sendMail (String to, String subject, String content, String contentType) {
        Message message = new MimeMessage(this.mailSession);
        Multipart multipart = new MimeMultipart();
        MimeBodyPart bodyPart = new MimeBodyPart();

        try {
            message.setFrom(new InternetAddress(this.config.getFrom()));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            bodyPart.setContent(content, contentType);
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception ex)  {
            log.error("sendMail() - " + ex.getMessage(), ex);
        }
    }

    public void setDebug (boolean debug) {
        this.mailSession.setDebug(debug);
    }

    private Properties getMailProperties (MailServiceConfig config) {
        Properties mailProperties = new Properties();

        mailProperties.put("mail.smtp.host", config.getHost());
        mailProperties.put("mail.smtp.port", config.getPort());
        mailProperties.put("mail.smtp.ssl.trust", config.getHost());
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.ssl.enable", "true");

        return mailProperties;
    }
}
