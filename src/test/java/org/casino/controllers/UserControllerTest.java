package org.casino.controllers;

import org.casino.models.User;
import org.casino.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowRegistrationForm() {
        String viewName = userController.showRegistrationForm(model);

        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setUsername("testUser");

        String viewName = userController.registerUser(user);

        assertEquals("redirect:/login?registered", viewName);
        verify(userService).saveUser(user);
    }

    @Test
    void testLogin() {
        String viewName = userController.login();

        assertEquals("login", viewName);
    }

    @Test
    void testLogout() {
        String viewName = userController.logout();

        assertEquals("logout", viewName);
    }
}
