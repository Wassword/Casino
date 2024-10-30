<<<<<<< Updated upstream
//package org.casino.controllers;
//
//import org.casino.models.User;
//import org.casino.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(HomeController.class)
//public class HomeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    // Test for the home page
//    @Test
//    public void testHome_ShouldReturnIndexViewWithWelcomeMessage() throws Exception {
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("index"))
//                .andExpect(model().attribute("welcomeMessage", "Welcome to the Casino!"));
//    }
//
//    // Test for the leaderboard page
//    @Test
//    public void testLeaderboard_ShouldReturnLeaderboardViewWithTopPlayers() throws Exception {
//        List<User> topPlayers = Arrays.asList(new User(), new User()); // Use default User constructor
//
//        when(userService.getTopPlayers()).thenReturn(topPlayers);
//
//        mockMvc.perform(get("/leaderboard"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("leaderboard"))
//                .andExpect(model().attribute("topPlayers", topPlayers));
//    }
//
//    // Test for leaderboard exception
//    @Test
//    public void testLeaderboard_ShouldReturnErrorViewOnException() throws Exception {
//        when(userService.getTopPlayers()).thenThrow(new RuntimeException("Error"));
//
//        mockMvc.perform(get("/leaderboard"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("error"))
//                .andExpect(model().attribute("errorMessage", "Unable to load the leaderboard. Please try again later."));
//    }
//}
=======
package org.casino.controllers;

import org.casino.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHome() {
        String viewName = homeController.home(model);

        assertEquals("index", viewName);
        verify(model).addAttribute("welcomeMessage", "Welcome to the Golden Grin Casino!");
    }

    @Test
    void testLeaderboard() {
        when(userService.getTopPlayers()).thenReturn(null);

        String viewName = homeController.leaderboard(model);

        assertEquals("leaderboard", viewName);
        verify(model).addAttribute("topPlayers", null);
    }
}
>>>>>>> Stashed changes
