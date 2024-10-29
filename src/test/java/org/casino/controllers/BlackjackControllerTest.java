package org.casino.controllers;

import org.casino.models.User;
import org.casino.service.BlackjackService;
import org.casino.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BlackjackController.class)
public class BlackjackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BlackjackService blackjackService;

    @BeforeEach
    public void setup() {
        // Mock necessary dependencies before each test
    }

    @Test
    public void testGame_ShouldReturnGameView() throws Exception {
        mockMvc.perform(get("/blackjack"))
                .andExpect(status().isOk())
                .andExpect(view().name("game"));
    }

    @Test
    public void testStartGame_ShouldReturnGameViewWithModelAttributes() throws Exception {
        User mockUser = new User();
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        // Return a String representing the player's hand
        when(blackjackService.getPlayerHand()).thenReturn(Collections.singletonList("Ace, King"));

        mockMvc.perform(post("/blackjack/start"))
                .andExpect(status().isOk())
                .andExpect(view().name("game"))
                .andExpect(model().attributeExists("status", "playerHand"));
    }

}
