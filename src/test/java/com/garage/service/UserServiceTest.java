package com.garage.service;

import com.mvgore.garageapi.entity.User;
import com.mvgore.garageapi.repository.UserRepository;
import com.mvgore.garageapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("Customer1");
        user.setEmail("customer1@example.com");
        user.setPassword("password");
        user.setRole(User.Role.CUSTOMER);
    }

    @Test
    void testFindByEmail() {
        when(userRepository.findByEmail("customer1@example.com")).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByEmail("customer1@example.com");
        assertTrue(result.isPresent());
        assertEquals("Customer1", result.get().getName());
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(user)).thenReturn(user);
        User saved = userService.saveUser(user);
        assertEquals("Customer1", saved.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testExistsByEmail() {
        when(userRepository.existsByEmail("customer1@example.com")).thenReturn(true);
        boolean exists = userRepository.existsByEmail("customer1@example.com");
        assertTrue(exists);
    }
}
