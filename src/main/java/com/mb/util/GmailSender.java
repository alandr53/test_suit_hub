package com.mb.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class GmailSender {
    public static void sendEmail(String recipient, String subject, String text) {
        // E-Mail-Einstellungen
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        final String myAccountEmail = "fombuffer1@gmail.com"; // Ihr Gmail-Konto
        final String password = "288866"; // Ihr Gmail-Passwort

        // Session erstellen
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        try {
            // Nachricht erstellen
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(text);

            // Nachricht senden
            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String recipient = "recipient@example.com"; // E-Mail des Empfängers
        String subject = "Test Email from Java";
        String text = "Hello, this is a test email sent from Java using Gmail!";
        sendEmail(recipient, subject, text);
    }
}
