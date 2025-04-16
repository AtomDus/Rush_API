package be.bdus.rush_api.api.models.mails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    public EmailDTO(String email, String msgBody, String subject) {
        this.recipient = email;
        this.msgBody = msgBody;
        this.subject = subject;
    }
}