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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void register(User user) {
        Optional<User> existingUserOpt = userRepository.findByEmail(user.getEmail());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (existingUser.getRole() == UserRole.NOT_USER) {
                // Mise à jour du compte temporaire avec les vraies infos
                existingUser.setFirstname(user.getFirstname());
                existingUser.setLastname(user.getLastname());
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                existingUser.setRole(UserRole.USER);
                existingUser.setAvailable(true);

                userRepository.save(existingUser);
                return;
            } else {
                throw new UserAlreadyExistExeption(HttpStatus.NOT_ACCEPTABLE, "User already exists");
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        user.setAvailable(true);
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
