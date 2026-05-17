package com.coffeeshop.controller;

import com.coffeeshop.dto.RegisterDTO;
import com.coffeeshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Authentication auth,
                            Model model) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("pageTitle", "Login");
        if (error != null)  model.addAttribute("errorMsg",   "Invalid email or password.");
        if (logout != null) model.addAttribute("successMsg", "You've been logged out.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("pageTitle", "Sign Up");
        model.addAttribute("registerForm", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterDTO form,
                           BindingResult result,
                           Model model,
                           RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Sign Up");
            return "auth/register";
        }

        try {
            userService.register(form);
            ra.addFlashAttribute("successMsg", "Account created! Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("pageTitle", "Sign Up");
            model.addAttribute("errorMsg", e.getMessage());
            return "auth/register";
        }
    }
}
