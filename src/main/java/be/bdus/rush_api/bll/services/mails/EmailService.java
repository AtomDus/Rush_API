package be.bdus.rush_api.bll.services.mails;

import be.bdus.rush_api.api.models.mails.EmailDTO;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import org.springframework.messaging.MessagingException;

import java.util.Map;

public interface EmailService {

    String sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables);

    public String sendStageReminderEmail(User user, Stage stage, String templateName, String subject);
}
