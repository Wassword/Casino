package org.casino.controllers;

import org.casino.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/blackjack")
public class BlackjackController {

    private static final Logger logger = LoggerFactory.getLogger(BlackjackController.class);

    private final BlackjackService blackjackService;

    @Autowired
    public BlackjackController(BlackjackService blackjackService) {
        this.blackjackService = blackjackService;
    }

    /**
     * Displays the initial game page.
     *
     * @return the game view.
     */
    @GetMapping("")
    public String game() {
        return "game";
    }

    /**
     * Starts a new Blackjack game and displays the initial state.
     *
     * @param model the model to hold game data.
     * @return the game view with initial game status.
     */
    @PostMapping("")
    public String startGame(Model model) {
        try {
            logger.info("Starting a new Blackjack game.");

            String gameStatus = blackjackService.startGame();
            logger.info("Game started successfully for user.");

            model.addAttribute("status", gameStatus);
            model.addAttribute("playerHand", blackjackService.getPlayerHand());
            model.addAttribute("dealerCard", blackjackService.getDealerFaceUpCard());

            return "game";
        } catch (NoSuchElementException e) {
            logger.error("Error starting game: {}", e.getMessage());
            model.addAttribute("errorMessage", "An error occurred while starting the game. Please try again.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error starting game: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Unexpected error. Please contact support.");
            return "error";
        }
    }

    /**
     * Handles the player "hit" action by adding a card to the player's hand.
     *
     * @param model the model to hold updated game data.
     * @return the updated game view.
     */
    @PostMapping("/hit")
    public String hit(Model model) {
        try {
            String hitResult = blackjackService.playerHit();

            model.addAttribute("status", hitResult);
            model.addAttribute("playerHand", blackjackService.getPlayerHand());
            model.addAttribute("dealerCard", blackjackService.getDealerFaceUpCard());

            return "game";
        } catch (IllegalStateException e) {
            logger.error("Error during hit action: {}", e.getMessage());
            model.addAttribute("errorMessage", "You cannot hit. The game may be over.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error during hit action: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Unexpected error. Please contact support.");
            return "error";
        }
    }

    /**
     * Handles the player "stand" action, concluding the game.
     *
     * @param model the model to hold final game data.
     * @return the result view with the final game outcome.
     */
    @PostMapping("/stand")
    public String stand(Model model) {
        try {
            String standResult = blackjackService.playerStand();

            model.addAttribute("status", standResult);
            model.addAttribute("playerHand", blackjackService.getPlayerHand());
            model.addAttribute("dealerHand", blackjackService.getDealerHand());

            return "result";
        } catch (IllegalStateException e) {
            logger.error("Error during stand action: {}", e.getMessage());
            model.addAttribute("errorMessage", "You cannot stand. The game may be over.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error during stand action: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Unexpected error. Please contact support.");
            return "error";
        }
    }

    /**
     * Retrieves and displays the current game status.
     *
     * @param model the model to hold game status data.
     * @return the current game view.
     */
    @GetMapping("/status")
    public String getGameStatus(Model model) {
        try {
            model.addAttribute("playerHand", blackjackService.getPlayerHand());
            model.addAttribute("dealerCard", blackjackService.getDealerFaceUpCard());

            return "game";
        } catch (NoSuchElementException e) {
            logger.error("Error retrieving game status: {}", e.getMessage());
            model.addAttribute("errorMessage", "Game status is unavailable.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error retrieving game status: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Unexpected error. Please contact support.");
            return "error";
        }
    }
}
