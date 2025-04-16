package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testFindById_Found() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L); // 👈 Forcer l'id

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId()); // ✅ ça ne plantera plus
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(2L);

        assertFalse(result.isPresent());
        verify(userRepository).findById(2L);
    }

    @Test
    void testFindByEmail_Found() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("nope@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail("nope@example.com");

        assertFalse(result.isPresent());
        verify(userRepository).findByEmail("nope@example.com");
    }

    @Test
    void testFindByPhoneNumber_Found() {
        String phone = "1234567890";
        User user = new User();
        user.setPhoneNumber(phone);
        when(userRepository.findByPhoneNumber(phone)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByPhoneNumber(phone);

        assertTrue(result.isPresent());
        assertEquals(phone, result.get().getPhoneNumber());
        verify(userRepository).findByPhoneNumber(phone);
    }

    @Test
    void testFindByPhoneNumber_NotFound() {
        when(userRepository.findByPhoneNumber("0000000000")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByPhoneNumber("0000000000");

        assertFalse(result.isPresent());
        verify(userRepository).findByPhoneNumber("0000000000");
    }

    @Test
    void testFindByJobTitle() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(new User()));
        when(userRepository.findByJobTitle(any(Pageable.class), eq("Director"))).thenReturn(userPage);

        Page<User> result = userService.findByJobTitle(Pageable.unpaged(), "Director");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository).findByJobTitle(any(Pageable.class), eq("Director"));
    }
}