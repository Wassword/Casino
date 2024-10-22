package org.casino.controllers;

import org.casino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        // You can pass any data to the model if needed
        model.addAttribute("welcomeMessage", "Welcome to the Casino!");

        // Return the name of the Thymeleaf template (home.html)
        return "index";
    }
    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        // Fetch top players and pass them to the view
        model.addAttribute("topPlayers", userService.getTopPlayers());
        return "leaderboard";
    }
    @GetMapping("/result")
    public String result(Model model) {
        // You can pass game result data if needed
        model.addAttribute("resultMessage", "Game Over! Check your final score.");
        return "result";
    }
}
