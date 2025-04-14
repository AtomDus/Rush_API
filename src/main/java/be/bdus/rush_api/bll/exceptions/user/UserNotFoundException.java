package be.bdus.rush_api.bll.exceptions.user;

import be.bdus.rush_api.bll.exceptions.GlobalException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GlobalException {

    public UserNotFoundException(HttpStatus status, Object error) {
        super(status, error);
    }
}
