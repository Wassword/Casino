package org.casino.controllers;

import org.casino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
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

    /**
     * Displays the leaderboard page with the top players.
     *
     * @param model the model to pass the top players to the view.
     * @return the name of the Thymeleaf template for the leaderboard.
     */
    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        try {
            model.addAttribute("topPlayers", userService.getTopPlayers());
            logger.info("Leaderboard accessed and top players fetched successfully.");
            return "leaderboard";
        } catch (Exception e) {
            logger.error("Error fetching top players for leaderboard: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Unable to load the leaderboard. Please try again later.");
            return "error";
        }
    }
}
