package org.casino.service;
import org.casino.models.User;
import org.casino.models.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Save new user after encoding password
    public void saveUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with this username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // Fetch user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get top 10 players by total wins
    public List<User> getTopPlayers() {
        return userRepository.findTop10ByOrderByTotalWinsDesc();
    }

    // Update user game status and balance after the game
    public void updateGameStatus(User user, boolean playerWins, int gameResultBalance) {
        if (playerWins) {
            user.setTotalWins(user.getTotalWins() + 1);  // Increase total wins if the player won
        }
        user.setBalance(user.getBalance() + gameResultBalance);  // Update balance based on game result
        userRepository.save(user);  // Save updated user details
    }

    // Place a bet and reduce user's balance accordingly
    public void placeBet(User user, int betAmount) {
        if (betAmount <= 0 || betAmount > user.getBalance()) {
            throw new IllegalArgumentException("Invalid bet amount");
        }
        user.setCurrentBet(betAmount);
        user.setBalance(user.getBalance() - betAmount);  // Reduce user's balance by the bet amount
        userRepository.save(user);  // Save the updated user state
    }

    // Clear user hand (e.g., when starting a new game round)
    public void clearUserHand(User user) {
        user.clearHand();  // Clear user's hand
        userRepository.save(user);  // Save the updated user state
    }
}