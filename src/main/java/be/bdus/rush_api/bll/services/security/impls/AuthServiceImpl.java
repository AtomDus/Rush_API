package be.bdus.rush_api.bll.services.security.impls;

import be.bdus.rush_api.bll.exceptions.user.BadCredentialsException;
import be.bdus.rush_api.bll.exceptions.user.UserAlreadyExistExeption;
import be.bdus.rush_api.bll.exceptions.user.UserNotFoundException;
import be.bdus.rush_api.bll.services.security.AuthService;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void register(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserNotFoundException(HttpStatus.NOT_ACCEPTABLE, "Bad credentials");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException(HttpStatus.NOT_ACCEPTABLE, "Bad credentials");
        }
        return user;
    }
}
