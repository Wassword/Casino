package org.casino.controllers;

import org.casino.service.BlackjackService;
import org.casino.service.UserService;
import org.casino.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BlackjackService blackjackService;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHome() {
        String viewName = homeController.home(model);

        assertEquals("index", viewName);
        verify(model).addAttribute("welcomeMessage", "Welcome to the Golden Grin Casino!");
    }

    @Test
    void testStandSuccess() {
        when(blackjackService.playerStand()).thenReturn("Player stood");
        when(blackjackService.getPlayerHand()).thenReturn(Collections.singletonList("Ace, King"));
        when(blackjackService.getDealerHand()).thenReturn(Collections.singletonList("Dealer's hand"));
        when(blackjackService.calculateHandValue()).thenReturn(21);

        String viewName = homeController.stand(model);

        assertEquals("result", viewName);
        verify(model).addAttribute("resultMessage", "Player stood");
        verify(model).addAttribute("playerHand", Collections.singletonList("Ace, King"));
        verify(model).addAttribute("dealerHand", Collections.singletonList("Dealer's hand"));
        verify(model).addAttribute("playerTotal", 21);
    }

    @Test
    void testStandIllegalState() {
        when(blackjackService.playerStand()).thenThrow(new IllegalStateException("Game over"));

        String viewName = homeController.stand(model);

        assertEquals("error", viewName);
        verify(model).addAttribute("errorMessage", "You cannot stand. The game may be over.");
    }

    @Test
    void testGetGameStatusSuccess() {
        when(blackjackService.getPlayerHand()).thenReturn(Collections.singletonList("Ace, King"));
        when(blackjackService.getDealerFaceUpCard()).thenReturn("Ace of Spades");

        String viewName = homeController.getGameStatus(model);

        assertEquals("game", viewName);
        verify(model).addAttribute("playerHand", Collections.singletonList("Ace, King"));
        verify(model).addAttribute("dealerCard", "Ace of Spades");
    }


    @Test
    void testShowBetPage() {
        User user = new User();
        user.setUsername("testUser");
        user.setBalance(1000);

        when(blackjackService.getLoggedInUsername()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(Optional.of(user));

        String viewName = homeController.showBetPage(model);

        assertEquals("bet", viewName);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("status", null);
    }
}
