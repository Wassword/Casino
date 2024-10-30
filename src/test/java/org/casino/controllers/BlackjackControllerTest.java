package org.casino.controllers;

import org.casino.models.User;
import org.casino.service.BlackjackService;
import org.casino.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

<<<<<<< Updated upstream
import java.util.Collections;
=======
import java.util.NoSuchElementException;
>>>>>>> Stashed changes
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BlackjackControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BlackjackService blackjackService;

    @Mock
    private Model model;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BlackjackController blackjackController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContext and Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock Authentication and UserDetails
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");

        user = new User();
        user.setUsername("testUser");
    }

    @Test
    void testGame() {
        String viewName = blackjackController.game();
        assertEquals("game", viewName);
    }

    @Test
    void testHitSuccess() {
        when(blackjackService.playerHit()).thenReturn("Hit success");

<<<<<<< Updated upstream
        // Return a String representing the player's hand
        when(blackjackService.getPlayerHand()).thenReturn(Collections.singletonList("Ace, King"));
=======
        String viewName = blackjackController.hit(model);
>>>>>>> Stashed changes

        assertEquals("game", viewName);
        verify(model).addAttribute("status", "Hit success");
    }

    @Test
    void testHitPlayerBusts() {
        when(blackjackService.playerHit()).thenReturn("Player busted");
        when(blackjackService.playerStand()).thenReturn("Player stood");

        String viewName = blackjackController.hit(model);

        assertEquals("result", viewName);
        verify(model).addAttribute("resultMessage", "Player stood");
    }

    @Test
    void testStandSuccess() {
        when(blackjackService.playerStand()).thenReturn("Player stood");

        String viewName = blackjackController.stand(model);

        assertEquals("result", viewName);
        verify(model).addAttribute("resultMessage", "Player stood");
    }

    @Test
    void testGetGameStatusSuccess() {
        when(blackjackService.getPlayerHand()).thenReturn(mock(java.util.List.class));
        when(blackjackService.getDealerFaceUpCard()).thenReturn("Ace of Spades");

        String viewName = blackjackController.getGameStatus(model);

        assertEquals("game", viewName);
        verify(model).addAttribute("playerHand", blackjackService.getPlayerHand());
        verify(model).addAttribute("dealerCard", "Ace of Spades");
    }


    @Test
    void testGetLoggedInUsername() {
        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");

        String username = blackjackController.getLoggedInUsername();

        assertEquals("testUser", username);
    }
}
