package org.casino.controllers;

import org.casino.models.User;
import org.casino.service.UserService;
import org.casino.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/blackjack")
public class BlackjackController {

    @Autowired
    private UserService userService;

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
    @PostMapping("/start")
    public String startGame(Model model) {
        String username = getLoggedInUsername();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        blackjackService.setUser(user);
        try {
            logger.info("Starting a new Blackjack game.");

            String gameStatus = blackjackService.startGame();
            logger.info("Game started successfully for user.");

            model.addAttribute("status", gameStatus);
            model.addAttribute("playerHandImages", blackjackService.getPlayerHand());
            model.addAttribute("dealerHand", blackjackService.getDealerHand());
            model.addAttribute("dealerFaceDownCard", blackjackService.getDealerFaceDownCard());
            model.addAttribute("dealerCardImage", blackjackService.getDealerFaceUpCard());



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
            model.addAttribute("dealerFaceDownCard", blackjackService.getDealerFaceDownCard());

            if (hitResult.contains("busted")) {
                String standResult = blackjackService.playerStand();

                model.addAttribute("resultMessage", standResult);
                model.addAttribute("playerHand", blackjackService.getPlayerHand());
                model.addAttribute("dealerHand", blackjackService.getDealerHand());
                model.addAttribute("playerTotal", blackjackService.calculateHandValue());
                return "result";  // Redirect to result page if player busts
            }


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

            model.addAttribute("resultMessage", standResult);
            model.addAttribute("playerHand", blackjackService.getPlayerHand());
            model.addAttribute("dealerHand", blackjackService.getDealerHand());
            model.addAttribute("playerTotal", blackjackService.calculateHandValue());
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
    @GetMapping("/bet")
    public String showBetPage(Model model) {
        String username = getLoggedInUsername();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);  // Pass the user to show balance
        model.addAttribute("status", null);  // Clear any previous status messages
        return "bet";  // Render bet.html
    }

    // Handle placing a bet
    @PostMapping("/place-bet")
    public String placeBet(@RequestParam("betAmount") int betAmount, Model model) {
        String username = getLoggedInUsername();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        try {
            blackjackService.setUser(user);
            blackjackService.placeBet(betAmount);  // Place the bet in the service

            // Start the game immediately after placing the bet
            String gameStatus = blackjackService.startGame();
            model.addAttribute("status", gameStatus);
            model.addAttribute("playerHand", blackjackService.getPlayerHand());
            model.addAttribute("dealerCard", blackjackService.getDealerFaceUpCard());
            model.addAttribute("gameStarted", true);  // Indicate that the game has started

            return "game";  // Redirect to game.html with game status
        } catch (IllegalArgumentException e) {
            model.addAttribute("status", e.getMessage());  // Display error message
            model.addAttribute("user", user);  // Keep user info to show balance
            return "bet";  // Reload bet.html with error
        }
    }


    public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

}
