package kafpinpin120.publishingHouse.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    public JavaMailSender emailSender;
    @Autowired
    private Environment environment;
    public void sendSimpleEmail(String toAddress, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(environment.getProperty("spring.mail.username"));
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        emailSender.send(simpleMailMessage);
    }

    public void sendEmailWithAttachment(String toAddress, String subject, String message, File attachmentFile) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(environment.getProperty("spring.mail.username"));
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(message);


        FileSystemResource file = new FileSystemResource(attachmentFile);
        helper.addAttachment(attachmentFile.getName(), file);

        emailSender.send(mimeMessage);
    }
}
