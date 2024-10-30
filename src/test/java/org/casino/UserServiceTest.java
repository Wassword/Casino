package org.casino;

import org.casino.models.User;
import org.casino.models.interfaces.UserRepository;
import org.casino.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setBalance(1000);
        user.setTotalWins(5);
    }

    @Test
    void testSaveUserSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testSaveUserUsernameTaken() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));

        assertEquals("User with this username already exists", exception.getMessage());
    }

    @Test
    void testFindByUsernameSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("testUser");

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("testUser");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testGetTopPlayers() {
        List<User> topPlayers = List.of(user);
        when(userRepository.findTop10ByOrderByTotalWinsDesc()).thenReturn(topPlayers);

        List<User> result = userService.getTopPlayers();

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
    }

    @Test
    void testUpdateGameStatusPlayerWins() {
        userService.updateGameStatus(user, true, 500);

        verify(userRepository, times(1)).save(user);
        assertEquals(6, user.getTotalWins()); // Incremented
        assertEquals(1500, user.getBalance()); // Updated balance
    }

    @Test
    void testUpdateGameStatusPlayerLoses() {
        userService.updateGameStatus(user, false, -500);

        verify(userRepository, times(1)).save(user);
        assertEquals(5, user.getTotalWins()); // Not incremented
        assertEquals(500, user.getBalance()); // Updated balance
    }

    @Test
    void testPlaceBetSuccess() {
        userService.placeBet(user, 500);

        verify(userRepository, times(1)).save(user);
        assertEquals(500, user.getBalance()); // Balance reduced
        assertEquals(500, user.getCurrentBet()); // Bet set
    }

    @Test
    void testPlaceBetInvalidAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.placeBet(user, 2000));

        assertEquals("Invalid bet amount", exception.getMessage());
    }

    @Test
    void testClearUserHand() {
        userService.clearUserHand(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testIsUsernameAvailable() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        boolean isAvailable = userService.isUsernameAvailable("newUser");

        assertTrue(isAvailable);
    }

    @Test
    void testIsUsernameTaken() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        boolean isAvailable = userService.isUsernameAvailable("testUser");

        assertFalse(isAvailable);
    }

    @Test
    void testResetBalance() {
        userService.resetBalance(user, 5000);

        verify(userRepository, times(1)).save(user);
        assertEquals(5000, user.getBalance());
    }
}
