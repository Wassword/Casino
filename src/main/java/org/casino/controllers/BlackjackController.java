package org.casino.controllers;

import org.casino.services.BlackjackService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blackjack")
public class BlackjackController {

    private final BlackjackService blackjackService;

    public BlackjackController(BlackjackService blackjackService) {
        this.blackjackService = blackjackService;
    }

    @PostMapping("/start")
    public String startGame() {
        return blackjackService.startGame();
    }

    @PostMapping("/hit")
    public String hit() {
        return blackjackService.playerHit();
    }

    @PostMapping("/stand")
    public String stand() {
        return blackjackService.playerStand();
    }

    @GetMapping("/status")
    public String getStatus() {
        return blackjackService.getGameStatus();
    }
}
