package org.casino;

import org.casino.controllers.HomeController;
import org.casino.models.User;  // Import the User class
import org.casino.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class HomeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void testLeaderboard() throws Exception {
        // Create mock User objects to return from the service
        User user1 = new User();
        user1.setUsername("Player1");
        user1.setTotalWins(5);

        User user2 = new User();
        user2.setUsername("Player2");
        user2.setTotalWins(3);

        User user3 = new User();
        user3.setUsername("Player3");
        user3.setTotalWins(2);

        // Mock the getTopPlayers() to return a list of users
        when(userService.getTopPlayers()).thenReturn(Arrays.asList(user1, user2, user3));

        // Perform the GET request and check the response
        mockMvc.perform(get("/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("leaderboard"))
                .andExpect(model().attribute("topPlayers", Arrays.asList(user1, user2, user3)));
    }
}
