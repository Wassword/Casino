package org.casino.controllers;

import org.casino.models.User;
import org.casino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // Simplified role assignment using UserService's role management
        userService.saveUser(user);
        return "redirect:/login?registered";
    }
    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    // Mapping for the logout page (logout.html)
//    @GetMapping("/logout")
//    public String logout() {
//        return "logout";
//    }
}
