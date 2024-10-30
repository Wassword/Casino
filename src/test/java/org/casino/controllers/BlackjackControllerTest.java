package org.casino.controllers;

import org.casino.service.BlackjackService;
import org.casino.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BlackjackControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BlackjackService blackjackService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BlackjackController blackjackController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContext and Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testHitSuccess() {
        when(blackjackService.playerHit()).thenReturn("Hit success");
        when(blackjackService.getPlayerHand()).thenReturn(Collections.singletonList("Ace, King"));
        when(blackjackService.getDealerFaceUpCard()).thenReturn("King of Hearts");
        when(blackjackService.isGameOver()).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = blackjackController.hit();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Hit success", response.getBody().get("status"));
        assertEquals(Collections.singletonList("Ace, King"), response.getBody().get("playerHandImages"));
        assertEquals("King of Hearts", response.getBody().get("dealerFaceUpCard"));
        assertFalse((Boolean) response.getBody().get("gameOver"));
    }

    @Test
    void testHitPlayerBusts() {
        when(blackjackService.playerHit()).thenReturn("Player busted");
        when(blackjackService.playerStand()).thenReturn("Player stood");
        when(blackjackService.getPlayerHand()).thenReturn(Collections.singletonList("Ace, King"));
        when(blackjackService.getDealerFaceUpCard()).thenReturn("King of Hearts");
        when(blackjackService.getDealerHand()).thenReturn(Collections.singletonList("Dealer's hand"));
        when(blackjackService.isGameOver()).thenReturn(false); // First call to check game status

        ResponseEntity<Map<String, Object>> response = blackjackController.hit();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Player busted", response.getBody().get("status"));
        assertEquals("Player stood", response.getBody().get("resultMessage"));
        assertEquals(Collections.singletonList("Dealer's hand"), response.getBody().get("dealerHandImages"));
        assertTrue((Boolean) response.getBody().get("gameOver"));
    }

    @Test
    void testHitGameAlreadyOver() {
        when(blackjackService.isGameOver()).thenReturn(true);
        when(blackjackService.getPlayerHand()).thenReturn(Collections.singletonList("Ace, King"));
        when(blackjackService.getDealerFaceUpCard()).thenReturn("King of Hearts");

        ResponseEntity<Map<String, Object>> response = blackjackController.hit();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Game is already over.", response.getBody().get("status"));
        assertEquals(Collections.singletonList("Ace, King"), response.getBody().get("playerHandImages"));
        assertEquals("King of Hearts", response.getBody().get("dealerFaceUpCard"));
        assertTrue((Boolean) response.getBody().get("gameOver"));
    }

    @Test
    void testHitUnexpectedError() {
        when(blackjackService.playerHit()).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Map<String, Object>> response = blackjackController.hit();

        assertEquals(500, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error. Please contact support.", response.getBody().get("errorMessage"));
    }
}
