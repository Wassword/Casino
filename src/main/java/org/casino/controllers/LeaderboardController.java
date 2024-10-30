package org.casino.controllers;

import org.casino.models.User;
import org.casino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blackjack/leaderboard")
public class LeaderboardController {

    private final UserService userService;

    @Autowired
    public LeaderboardController(UserService userService) {
        this.userService = userService;
    }

    // PUT endpoint to get users ordered by total wins
    @PutMapping("/update-wins")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateLeaderboardByWins() {
        List<User> users = userService.getUsersOrderedByWins();

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);

        return ResponseEntity.ok(response);
    }
    /**
     * Displays the leaderboard page with the top players.
     *
     * @param model the model to pass the top players to the view.
     * @return the name of the Thymeleaf template for the leaderboard.
     */
    @GetMapping
    public String leaderboard(Model model) {
        try {
            List<User> users = userService.getUsersOrderedByChips();
            model.addAttribute("users", users);
            return "leaderboard"; // Return the leaderboard.html template


        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unable to load the leaderboard. Please try again later.");
            return "error";
        }

    }
}
