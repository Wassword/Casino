package org.casino.config;

import org.casino.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/login", "/register", "/error", "/css/**", "/js/**").permitAll() // Public pages
                                .requestMatchers("/menu-management").hasAnyRole("STAFF", "ADMIN")
                                .requestMatchers("/orders-processing").hasRole("STAFF")
                                .requestMatchers("/table-management").hasRole("STAFF")
                                .requestMatchers("/inventory-management").hasRole("ADMIN")
                                .requestMatchers("/sales-report").hasRole("ADMIN")
                                .requestMatchers("/api/inventory/**").hasRole("ADMIN") // Securing Inventory API
                                .requestMatchers("/api/orders/**").hasRole("STAFF") // Securing Orders API
                                .requestMatchers("/tables/**").hasRole("STAFF") // Securing Tables API
                                .anyRequest().authenticated() // Ensure other requests are authenticated
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/dashboard", true) // Redirect to dashboard on successful login
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }
}
