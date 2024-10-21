package org.casino.controllers;

import org.casino.service.BlackjackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blackjack")
public class BlackjackController {

    private final BlackjackService blackjackService;

    public BlackjackController(BlackjackService blackjackService) {
        this.blackjackService = blackjackService;
    }

    // Start the game and return a view
    @PostMapping("/start")
    public String startGame(Model model) {
        String gameStatus = blackjackService.startGame();
        model.addAttribute("status", gameStatus);  // Add game status to the model
        return "game";  // Return the view name (e.g., a Thymeleaf HTML file called blackjack.html)
    }

    // Player hits and returns the updated view
    @PostMapping("/hit")
    public String hit(Model model) {
        String hitResult = blackjackService.playerHit();
        model.addAttribute("status", hitResult);  // Add the updated game status to the model
        return "game";  // Return the same view to display the updated game status
    }

    // Player stands and returns the updated view
    @PostMapping("/stand")
    public String stand(Model model) {
        String standResult = blackjackService.playerStand();
        model.addAttribute("status", standResult);  // Add the final game result to the model
        return "result";  // Return the view for the final result
    }

    // Get the game status and display the current status view
    @GetMapping("/status")
    public String getStatus(Model model) {
        String gameStatus = blackjackService.getGameStatus();
        model.addAttribute("status", gameStatus);  // Add the current game status to the model
        return "game";  // Return the view to display the current game status
    }
}
