package org.casino;

import org.casino.controllers.BlackjackController;
import org.casino.service.BlackjackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class BlackjackControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BlackjackService blackjackService;

    @InjectMocks
    private BlackjackController blackjackController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(blackjackController).build();  // Set up MockMvc
    }

    @Test
    void testGameStart_withTwoCards() throws Exception {
        when(blackjackService.startGame()).thenReturn("Player starts with two cards: Ace of Spades, King of Hearts");

        mockMvc.perform(get("/blackjack"))
                .andExpect(status().isOk())
                .andExpect(view().name("game"));
    }

    @Test
    void testPlayerHit() throws Exception {
        when(blackjackService.playerHit()).thenReturn("Player hits and draws: 7 of Diamonds");

        mockMvc.perform(post("/blackjack/hit"))
                .andExpect(status().isOk())
                .andExpect(view().name("game"))
                .andExpect(model().attribute("status", "Player hits and draws: 7 of Diamonds"));
    }

    @Test
    void testPlayerStand() throws Exception {
        when(blackjackService.playerStand()).thenReturn("Player stands. Dealer wins with 20");

        mockMvc.perform(post("/blackjack/stand"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attribute("status", "Player stands. Dealer wins with 20"));
    }
}
