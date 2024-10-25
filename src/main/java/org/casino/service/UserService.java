package org.casino.service;

import org.casino.models.User;
import org.casino.models.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a new user after encoding the password. Throws an exception if the username is already taken.
     *
     * @param user the user to save.
     * @throws IllegalArgumentException if a user with the same username already exists.
     */
    public void saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with this username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("New user created with username: {}", user.getUsername());
    }

    /**
     * Finds a user by username.
     *
     * @param username the username to search for.
     * @return an Optional containing the user if found.
     */
    public Optional<User> findByUsername(String username) {
        logger.debug("Fetching user with username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieves the top 10 players by total wins.
     *
     * @return a list of the top 10 users by total wins.
     */
    public List<User> getTopPlayers() {
        logger.debug("Retrieving top 10 players by total wins.");
        return userRepository.findTop10ByOrderByTotalWinsDesc();
    }

    /**
     * Updates the game status and balance of the user after a game round.
     *
     * @param user             the user to update.
     * @param playerWins       true if the player won the game, false otherwise.
     * @param gameResultBalance the balance adjustment based on the game result.
     */
    public void updateGameStatus(User user, boolean playerWins, int gameResultBalance) {
        if (playerWins) {
            user.setTotalWins(user.getTotalWins() + 1);
            logger.info("User {} won the game. Total wins: {}", user.getUsername(), user.getTotalWins());
        }
        user.setBalance(user.getBalance() + gameResultBalance);
        userRepository.save(user);
        logger.info("Updated user balance to {}", user.getBalance());
    }

    /**
     * Places a bet for the user, reducing their balance accordingly.
     *
     * @param user      the user placing the bet.
     * @param betAmount the amount to bet.
     * @throws IllegalArgumentException if the bet amount is invalid.
     */
    public void placeBet(User user, int betAmount) {
        if (betAmount <= 0 || betAmount > user.getBalance()) {
            throw new IllegalArgumentException("Invalid bet amount");
        }
        user.setCurrentBet(betAmount);
        user.setBalance(user.getBalance() - betAmount);
        userRepository.save(user);
        logger.info("User {} placed a bet of {}. New balance: {}", user.getUsername(), betAmount, user.getBalance());
    }

    /**
     * Clears the user's hand, usually when starting a new game round.
     *
     * @param user the user whose hand is to be cleared.
     */
    public void clearUserHand(User user) {
        user.clearHand();
        userRepository.save(user);
        logger.debug("User {}'s hand cleared", user.getUsername());
    }

    /**
     * Checks if the username is available (not taken).
     *
     * @param username the username to check.
     * @return true if the username is available, false otherwise.
     */
    public boolean isUsernameAvailable(String username) {
        boolean available = userRepository.findByUsername(username).isEmpty();
        logger.debug("Username {} availability: {}", username, available);
        return available;
    }

    /**
     * Resets a user's balance to a default starting amount.
     *
     * @param user the user whose balance is to be reset.
     * @param defaultBalance the default balance to reset to.
     */
    public void resetBalance(User user, int defaultBalance) {
        user.setBalance(defaultBalance);
        userRepository.save(user);
        logger.info("User {}'s balance reset to {}", user.getUsername(), defaultBalance);
    }
}
