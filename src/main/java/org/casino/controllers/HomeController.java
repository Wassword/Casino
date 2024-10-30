package org.casino.controllers;

import org.casino.models.User;
import org.casino.service.BlackjackService;
import org.casino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final UserService userService;
    private final BlackjackService blackjackService;

    @Autowired
    public HomeController(UserService userService, BlackjackService blackjackService) {
        this.userService = userService;
        this.blackjackService = blackjackService;
    }

    /**
     * Displays the home page with a welcome message.
     *
     * @param model the model to pass attributes to the view.
     * @return the name of the Thymeleaf template for the home page.
     */
    @GetMapping("/")
    public String home(Model model) {
        String welcomeMessage = "Welcome to the Golden Grin Casino!";
        model.addAttribute("welcomeMessage", welcomeMessage);
        logger.info("Home page accessed with welcome message: {}", welcomeMessage);
        return "index";
    }


    @PostMapping("blackjack/stand")
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
    @GetMapping("/blackjack")
    public String showGamePage(Model model) {
        String gameStatus = blackjackService.startGame();

        model.addAttribute("status", gameStatus);
        model.addAttribute("playerHandImages", blackjackService.getPlayerHand());
        model.addAttribute("dealerHand", blackjackService.getDealerHand());
        model.addAttribute("dealerFaceDownCard", blackjackService.getDealerFaceDownCard());
        model.addAttribute("dealerFaceUpCard", blackjackService.getDealerFaceUpCard());

        return "game";
    }
    @GetMapping("blackjack/bet")
    public String showBetPage(Model model) {
        String username = blackjackService.getLoggedInUsername();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);  // Pass the user to show balance
        model.addAttribute("status", null);  // Clear any previous status messages
        return "bet";  // Render bet.html
    }
    @PostMapping("/blackjack")
    public String placeBetAndStartGame(@RequestParam("betAmount") int betAmount, Model model) {
        String username = blackjackService.getLoggedInUsername();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // Set user in blackjack service and place the bet
            blackjackService.setUser(user);
            blackjackService.placeBet(betAmount);

            // Start the game after placing the bet
            String gameStatus = blackjackService.startGame();

            // Add attributes to model to pass to the HTML template
            model.addAttribute("status", gameStatus);
            model.addAttribute("playerHandImages", blackjackService.getPlayerHand());
            model.addAttribute("dealerFaceUpCard", blackjackService.getDealerFaceUpCard());
            model.addAttribute("gameStarted", true);  // Indicate game has started

            return "game";  // Return "game.html" with game status and initial hand

        } catch (IllegalArgumentException e) {
            // Handle invalid bet by adding error message and user balance to the model
            model.addAttribute("status", e.getMessage());
            model.addAttribute("userBalance", user.getBalance());
            return "bet";  // Return "bet.html" to display the error message and balance
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unexpected error occurred. Please contact support.");
            return "error";  // Return an error page in case of unexpected errors
        }
    }

    @GetMapping("blackjack/results")
    public String getresults(Model model) {
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






}
