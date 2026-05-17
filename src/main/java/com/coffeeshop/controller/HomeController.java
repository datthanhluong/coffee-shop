package com.coffeeshop.controller;

import com.coffeeshop.service.CartService;
import com.coffeeshop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("activePage", "home");
        model.addAttribute("featuredProducts", productService.getFeatured());
        model.addAttribute("cartCount", cartService.getItemCount(session));
        return "index";
    }

    @GetMapping("/403")
    public String accessDenied(Model model) {
        model.addAttribute("pageTitle", "Access Denied");
        return "error/403";
    }
}
