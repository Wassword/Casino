//package org.casino;
//
//import org.casino.service.BlackjackService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.ui.Model;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(BlackjackController.class)
//public class BlackjackControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BlackjackService blackjackService;
//
//    @Mock
//    private Model model;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testGameView() throws Exception {
//        mockMvc.perform(get("/blackjack"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("game"));
//    }
//    // Test to ensure GET request to "/blackjack" returns the game view.
//
//    @Test
//    public void testStartGame() throws Exception {
//        when(blackjackService.startGame()).thenReturn("Game Started");
//
//        mockMvc.perform(post("/blackjack/start"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("status"))
//                .andExpect(model().attribute("status", "Game Started"))
//                .andExpect(view().name("game"));
//
//        verify(blackjackService, times(1)).startGame();
//    }
//    // Test to ensure POST request to "/blackjack/start" starts the game, sets model attribute, and returns the game view.
//
//    @Test
//    public void testHit() throws Exception {
//        when(blackjackService.playerHit()).thenReturn("Player Hits");
//
//        mockMvc.perform(post("/blackjack/hit"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("status"))
//                .andExpect(model().attribute("status", "Player Hits"))
//                .andExpect(view().name("game"));
//
//        verify(blackjackService, times(1)).playerHit();
//    }
//    // Test to ensure POST request to "/blackjack/hit" processes hit action, sets model attribute, and returns the game view.
//
//    @Test
//    public void testStand() throws Exception {
//        when(blackjackService.playerStand()).thenReturn("Player Stands");
//
//        mockMvc.perform(post("/blackjack/stand"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("status"))
//                .andExpect(model().attribute("status", "Player Stands"))
//                .andExpect(view().name("result"));
//
//        verify(blackjackService, times(1)).playerStand();
//    }
//    // Test to ensure POST request to "/blackjack/stand" processes stand action, sets model attribute, and returns the result view.
//
//    @Test
//    public void testGetStatus() throws Exception {
//        when(blackjackService.getGameStatus(1L)).thenReturn("Game In Progress");
//
//        mockMvc.perform(get("/blackjack/status").param("gameId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("status"))
//                .andExpect(model().attribute("status", "Game In Progress"))
//                .andExpect(view().name("game"));
//
//        verify(blackjackService, times(1)).getGameStatus(1L);
//    }
//    // Test to ensure GET request to "/blackjack/status" fetches the game status, sets model attribute, and returns the game view.
//}
