package be.bdus.rush_api.bll.services.mails.impls;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import be.bdus.rush_api.api.models.mails.EmailDTO;
import be.bdus.rush_api.bll.services.mails.EmailService;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.File;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendSimpleMail(EmailDTO details) {
        try {
            Context context = new Context();
            context.setVariables(Map.of(
                    "subject", details.getSubject(),
                    "msgBody", details.getMsgBody()
            ));

            String emailContent = templateEngine.process("emails/email-with-attachment", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setSubject(details.getSubject());
            mimeMessageHelper.setText(emailContent, true);

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            mimeMessageHelper.addInline("qrCodeImage", file);

            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            javaMailSender.send(mimeMessage);
            return "Mail sent successfully with attachment";
        } catch (MessagingException e) {
            return "Error while sending mail: " + e.getMessage();
        }
    }

    public String sendEventReminderEmail(User user, Stage stage){
        try {
            Context context = new Context();
            context.setVariables(Map.of(
                    "Email", user.getEmail(),
                    "stageName", stage.getName(),
                    "finishingDate", stage.getFinishingDate().toString(),
                    "stageResponsable", stage.getResponsable()
            ));

            String emailContent = templateEngine.process("emails/event-reminder", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Rappel de votre événement : " + stage.getName());
            helper.setText(emailContent, true);

            javaMailSender.send(message);

            return "Email sent successfully";
        }
        catch (MessagingException e) {
            return "Error while sending email: " + e.getMessage();
        }
    }
}
