package service;

import authenticator.EmailAuthenticator;
import utils.EmailUtil;

import javax.mail.Session;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {

    private final Session mailSession;

    public EmailService() {
        Properties props = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mail.properties")) {
            props.load(inputStream);
        } catch (IOException e) {
            System.out.println("Error loading properties file");
        }


        String username = props.getProperty("mail.username");
        String password = props.getProperty("mail.password");

        EmailAuthenticator auth = new EmailAuthenticator(username, password);

        this.mailSession = Session.getInstance(props, auth);
    }

    public void sendEmail(String fromEmail, String toEmail, String subject, String body) {
        EmailUtil.sendEmail(this.mailSession, fromEmail, toEmail, subject, body);
    }
}