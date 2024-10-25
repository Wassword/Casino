package org.casino.service;

import org.casino.models.User;
import org.casino.models.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username and returns a UserDetails object for Spring Security authentication.
     * If the user is not found, throws UsernameNotFoundException.
     *
     * @param username the username of the user to load.
     * @return a UserDetails object representing the authenticated user.
     * @throws UsernameNotFoundException if the user cannot be found by the given username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);

        // Retrieve the user from the repository
        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user = optionalUser.orElseThrow(() -> {
            logger.error("User with username '{}' not found", username);
            return new UsernameNotFoundException("User not found");
        });

        logger.info("User found: {}. Enabled: {}", user.getUsername(), user.getEnabled());

        // Create UserDetails instance with user information
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.getEnabled()) // Disable if user is not enabled
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities(Collections.emptyList()) // No authorities/roles for now
                .build();
    }
}
