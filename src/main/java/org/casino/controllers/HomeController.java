package org.casino.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // You can pass any data to the model if needed
        model.addAttribute("welcomeMessage", "Welcome to the Casino!");

        // Return the name of the Thymeleaf template (home.html)
        return "index";
    }
}
