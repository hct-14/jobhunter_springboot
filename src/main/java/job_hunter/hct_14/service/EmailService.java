package job_hunter.hct_14.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import job_hunter.hct_14.entity.response.email.ResEmailJob;
import job_hunter.hct_14.repository.JobReponsetory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EmailService {
    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final JobReponsetory jobReponsetory;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(MailSender mailSender, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, JobReponsetory jobReponsetory) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.jobReponsetory = jobReponsetory;
    }
//
//    public void sendEmail() {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo("mlinhh08@gmail.com");
//        message.setSubject("ChuaBiet01");
//        message.setText("lam nhay thui");
//        message.setFrom(fromEmail);
//        mailSender.send(message);
//    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart,
                              boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
             isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            message.setFrom(fromEmail);

            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e);
        }

    }
    @Async
    public void sendEmailFromTemplateSync(String to, String subject, String templateName, String username, List<ResEmailJob> value) {
        Context context = new Context();
//        List<Job> arrJob = this.jobReponsetory.findAll();
//        String name = "hct14";

        context.setVariable("name", username);
        context.setVariable("jobs", value);
        String content = this.templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
    }
}
