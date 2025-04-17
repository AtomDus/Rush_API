package be.bdus.rush_api.bll.services.impls;

import be.bdus.rush_api.dal.repositories.UserRepository;
import be.bdus.rush_api.dl.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
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

    @Test
    void testFindAvailableUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(new User(), new User());
        Page<User> page = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findByAvailableTrue(pageable)).thenReturn(page);

        Page<User> result = userService.findAvailableUsers(pageable);

        assertEquals(2, result.getTotalElements());
        verify(userRepository).findByAvailableTrue(pageable);
    }

    @Test
    void testSetUserAvailable_success() {
        Long userId = 1L;
        User user = new User();
        user.setAvailable(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        boolean result = userService.setUserAvailable(userId);

        assertTrue(result);
        assertTrue(user.isAvailable());
        verify(userRepository).save(user);
    }


    @Test
    void testSetUserAvailable_userNotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        boolean result = userService.setUserAvailable(userId);

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }



    @Test
    void testSetUserAvailable_found() {
        // Given
        User user = new User();
        user.setAvailable(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = userService.setUserAvailable(1L);

        // Then
        assertTrue(result);
        assertTrue(user.isAvailable());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void testSetUserAvailable_notFound() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        boolean result = userService.setUserAvailable(99L);

        // Then
        assertFalse(result);
        verify(userRepository).findById(99L);
        verify(userRepository, never()).save(any());
    }

}