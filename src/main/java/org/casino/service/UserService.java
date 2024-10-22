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

    public List<User> getTopPlayers() {
        // Fetch top 10 players by totalWins in descending order
        return userRepository.findTop10ByOrderByTotalWinsDesc();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with this username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}

