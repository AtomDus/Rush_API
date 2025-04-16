package be.bdus.rush_api.bll.services.mails;

import be.bdus.rush_api.api.models.mails.EmailDTO;
import be.bdus.rush_api.dl.entities.Stage;
import be.bdus.rush_api.dl.entities.User;
import org.springframework.messaging.MessagingException;

public interface EmailService {

    String sendSimpleMail(EmailDTO details);

    String sendEventReminderEmail(User user, Stage stage) throws MessagingException;
}
