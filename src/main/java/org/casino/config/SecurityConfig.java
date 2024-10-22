package org.casino.config;

import org.casino.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Service to load user-specific data
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();  // Your custom user details service
    }

    // Password encoder to hash passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider with userDetailsService and password encoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Configure authentication manager
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()  // Disable CSRF for simplicity
                .authorizeRequests()
                .requestMatchers("/blackjack/**").authenticated()  // Requires authentication for Blackjack endpoints
                .anyRequest().permitAll()  // Allows unrestricted access to all other endpoints
                .and()
                .formLogin()  // Enable form-based login
                .loginPage("/login").permitAll()  // Custom login page, if you implement it
                .and()
                .logout()  // Enable logout functionality
                .permitAll()
                .and()
                .build();
    }

    // Configuring authentication provider
    @Bean
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
