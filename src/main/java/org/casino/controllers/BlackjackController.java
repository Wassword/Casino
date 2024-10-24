package org.casino.controllers;

import org.casino.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blackjack")
public class BlackjackController {

    private final BlackjackService blackjackService;

    @Autowired
    public BlackjackController(BlackjackService blackjackService) {
        this.blackjackService = blackjackService;
    }


    @GetMapping("")
    public String game() {
        return "game";
    }
    // Start the game and return the initial game view
    @PostMapping("/start")
    public String startGame(Model model) {
        String gameStatus = blackjackService.startGame(); // Start the game using the service
        model.addAttribute("status", gameStatus);  // Add game status to the model
        model.addAttribute("playerHand", blackjackService.getPlayerHand());  // Add player's hand
        model.addAttribute("dealerCard", blackjackService.getDealerFaceUpCard());  // Add dealer's face-up card
        return "game";  // Ensure 'game.html' exists in 'src/main/resources/templates/'
    }

    // Player hits and returns the updated view
    @PostMapping("/hit")
    public String hit(Model model) {
        String hitResult = blackjackService.playerHit();
        model.addAttribute("status", hitResult);  // Add the updated game status to the model
        model.addAttribute("playerHand", blackjackService.getPlayerHand());  // Add player's updated hand
        model.addAttribute("dealerCard", blackjackService.getDealerFaceUpCard());  // Add dealer's face-up card
        return "game";  // Return the same view to display the updated game status
    }

    // Player stands and returns the final result view
    @PostMapping("/stand")
    public String stand(Model model) {
        String standResult = blackjackService.playerStand();
        model.addAttribute("status", standResult);  // Add the final game result to the model
        model.addAttribute("playerHand", blackjackService.getPlayerHand());  // Add player's final hand
        model.addAttribute("dealerHand", blackjackService.getDealerFaceUpCard());  // Add dealer's final hand
        return "result";  // Return the view for the final result
    }

    // Get current game status (optional)
    @GetMapping("/status")
    public String getGameStatus(Model model) {
        model.addAttribute("playerHand", blackjackService.getPlayerHand());  // Add player's hand to the model
        model.addAttribute("dealerCard", blackjackService.getDealerFaceUpCard());  // Add dealer's face-up card
        return "game";  // Return the current game status view
    }
}
