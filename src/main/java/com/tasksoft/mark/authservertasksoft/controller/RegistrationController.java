package com.tasksoft.mark.authservertasksoft.controller;

import com.tasksoft.mark.authservertasksoft.dto.UserRegistrationDto;
import com.tasksoft.mark.authservertasksoft.security.SecuredUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final SecuredUserService securedDetailsService;

    public RegistrationController(SecuredUserService securedDetailsService) {
        this.securedDetailsService = securedDetailsService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register/save")
    public String saveUser(@ModelAttribute("user") UserRegistrationDto userRegistrationDto) throws Exception {
        securedDetailsService.registerUser(userRegistrationDto).orElseThrow(() -> new Exception("User registration failed"));
        return "redirect:/login?registered=true";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
