package be.bdus.rush_api.bll.services.mails.impls;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import be.bdus.rush_api.bll.services.mails.EmailService;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);

            String emailContent = templateEngine.process(templateName, context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailContent, true);

            javaMailSender.send(message);

            return "Email sent successfully";
        } catch (MessagingException e) {
            return "Error while sending email: " + e.getMessage();
        }
    }

    public String sendStageReminderEmail(User user, Stage stage, String templateName, String subject) {
        try {
            Context context = new Context();
            context.setVariables(Map.of(
                    "email", user.getEmail(),
                    "stageName", stage.getName(),
                    "finishingDate", stage.getFinishingDate().toString(),
                    "stageResponsable", stage.getResponsable().getFullName()
            ));

            String emailContent = templateEngine.process(templateName, context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(emailContent, true);

            javaMailSender.send(message);

            return "Email sent successfully";
        } catch (MessagingException e) {
            return "Error while sending email: " + e.getMessage();
        }
    }
}
