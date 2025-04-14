package be.bdus.rush_api.bll.exceptions.user;
import be.bdus.rush_api.bll.exceptions.GlobalException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistExeption extends GlobalException {

    public UserAlreadyExistExeption(HttpStatus status, Object error) {
        super(status, error);
    }
}
