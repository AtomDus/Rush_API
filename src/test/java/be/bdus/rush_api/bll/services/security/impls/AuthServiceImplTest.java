package be.bdus.rush_api.bll.services.security.impls;

import be.bdus.rush_api.bll.exceptions.user.BadCredentialsException;
import be.bdus.rush_api.bll.exceptions.user.UserAlreadyExistExeption;
import be.bdus.rush_api.bll.exceptions.user.UserNotFoundException;
import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.User;
import be.bdus.rush_api.dl.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // <- C'est ça qui manquait !
    }

    @Test
    void testRegister_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("plainPassword");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        authService.register(user);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(UserRole.USER, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void testRegister_AlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistExeption.class, () -> authService.register(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLogin_Success() {
        String email = "test@example.com";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        User result = authService.login(email, rawPassword);

        assertEquals(user, result);
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> authService.login("unknown@example.com", "password"));
    }

    @Test
    void testLogin_BadPassword() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login(email, "wrongPassword"));
    }
}