package org.merchantech.nftproject.helpers;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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

@Component
public class MailService {
	
    private Session mailSession;
    private Environment environment;

    @Autowired
    public MailService (Environment environment) {
        Properties mailProperties = this.getMailProperties(environment);
        String user = environment.getProperty("mail.smtp.user");
        String password = environment.getProperty("mail.smtp.password");

        this.environment = environment;

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
        String from = this.environment.getProperty("mail.smtp.from");

        try {
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            bodyPart.setContent(content, contentType);
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception ex)  {
            System.err.println("[sendMail-Exception] " + ex.getMessage());
        }
    }

    public void setDebug (boolean debug) {
        this.mailSession.setDebug(debug);
    }

    private Properties getMailProperties (Environment environment) {
        Properties mailProperties = new Properties();

        mailProperties.put("mail.smtp.host", environment.getProperty("mail.smtp.host"));
        mailProperties.put("mail.smtp.port", environment.getProperty("mail.smtp.port"));
        mailProperties.put("mail.smtp.ssl.trust", environment.getProperty("mail.smtp.host"));
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.ssl.enable", "true");

        return mailProperties;
    }
}
