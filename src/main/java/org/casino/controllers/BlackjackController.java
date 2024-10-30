package org.casino.controllers;

import org.casino.models.User;
import org.casino.service.UserService;
import org.casino.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/blackjack")
public class BlackjackController {

    private static final Logger logger = LoggerFactory.getLogger(BlackjackController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BlackjackService blackjackService;


    /**
     * Processes the "hit" action.
     */
    @PutMapping("/hit")
    public ResponseEntity<Map<String, Object>> hit() {
        Map<String, Object> response = new HashMap<>();

        try {
            if (blackjackService.isGameOver()) {
                response.put("status", "Game is already over.");
                response.put("playerHandImages", blackjackService.getPlayerHand());
                response.put("dealerFaceUpCard", blackjackService.getDealerFaceUpCard());
                response.put("gameOver", true);
                return ResponseEntity.ok(response);
            }

            String hitResult = blackjackService.playerHit();
            response.put("status", hitResult);
            response.put("playerHandImages", blackjackService.getPlayerHand());
            response.put("dealerFaceUpCard", blackjackService.getDealerFaceUpCard());

            if (hitResult.contains("busted")) {
                String standResult = blackjackService.playerStand();
                response.put("resultMessage", standResult);
                response.put("dealerHandImages", blackjackService.getDealerHand());
                response.put("gameOver", true);
            } else {
                response.put("gameOver", false);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("errorMessage", "Unexpected error. Please contact support.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/double-down")
    public ResponseEntity<Map<String, Object>> doubleDown() {
        Map<String, Object> response = new HashMap<>();
        try {
            String doubleDownResult = blackjackService.playerDoubleDown();

            response.put("status", doubleDownResult);
            response.put("playerHandImages", blackjackService.getPlayerHand());
            response.put("dealerFaceUpCard", blackjackService.getDealerFaceUpCard());
            response.put("gameOver", doubleDownResult.contains("busted") || doubleDownResult.contains("win"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("errorMessage", "Unexpected error occurred. Please contact support.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
