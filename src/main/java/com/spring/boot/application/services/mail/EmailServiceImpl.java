package com.spring.boot.application.services.mail;

import com.spring.boot.application.common.enums.AccountType;
import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.utils.AppUtil;
import com.spring.boot.application.entity.User;
import com.spring.boot.application.repositories.UserRepository;
import com.spring.boot.application.services.mail.thymleaf.ThymeleafService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService{
    final private UserRepository userRepository;

    final private JavaMailSender mailSender;

    final private ThymeleafService thymeleafService;

    @Value(value = "${spring.mail.username}")
    private String email;

    @Value(value = "${project.sources}")
    private String root;

    public EmailServiceImpl(UserRepository userRepository, JavaMailSender mailSender, ThymeleafService thymeleafService) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.thymeleafService = thymeleafService;
    }

    @Override
    public void confirmRegisterAccount(User user) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("full_name", user.getFirstName() + " " + user.getLastName());
            variables.put("link", "http://localhost:4201/verify-email/" + user.getActiveCode());
            variables.put("logo", AppUtil.getLogo());

            File attachment = new File(root + "images/TopWork-log0.png");
            FileSystemResource file = new FileSystemResource(attachment);
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom(email);
            helper.setTo(user.getEmail());
            helper.setSubject("Verify email");
            helper.addInline(attachment.getName(), file);

            helper.setText(thymeleafService.createdContent("confirm-register-account.html", variables), true);

            mailSender.send(message);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void resetPassword(String activeCode, AccountType type) {
        try {
            User user = userRepository.getByActiveCodeAndStatus(activeCode, Status.ACTIVE);
            Map<String, Object> variables = variable(type, user);

            File attachment = new File(root + "images/TopWork-log0.png");
            FileSystemResource file = new FileSystemResource(attachment);
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setFrom(email);
            helper.setTo(user.getEmail());
            helper.setSubject("Reset password");
            helper.addInline(attachment.getName(), file);

            helper.setText(thymeleafService.createdContent("reset-password.html", variables), true);

            mailSender.send(message);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private MimeMessageHelper getMimeMessageHelper(User user, MimeMessage message) throws MessagingException {
        File attachment = new File(root + "images/TopWork-log0.png");
        FileSystemResource file = new FileSystemResource(attachment);

        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setFrom(email);
        helper.setTo(user.getEmail());
        helper.setSubject("Verify email");
        helper.addAttachment(attachment.getName(), file);
        return helper;
    }

    private Map<String, Object> variable(AccountType type, User user) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("full_name", user.getFirstName() + " " + user.getLastName());
        variables.put("logo", AppUtil.getLogo());
        if (AccountType.ADMIN_SYSTEM.equals(type)) {
            variables.put("link", "http://localhost:4200/reset-password/" + user.getActiveCode());
        }

        if (AccountType.COMPANY.equals(type)) {
            variables.put("link", "http://localhost:4202/reset-password/" + user.getActiveCode());
        }

        if (AccountType.CANDIDATE.equals(type)) {
            variables.put("link", "http://localhost:4201/reset-password/" + user.getActiveCode());
        }

        return variables;
    }
}
