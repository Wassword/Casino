package org.casino.controllers;

import org.casino.service.UserService;
import org.casino.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/blackjack")
public class BlackjackController {

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
            response.put("exceptionMessage", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
