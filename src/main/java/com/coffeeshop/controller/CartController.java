package com.coffeeshop.controller;

import com.coffeeshop.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        model.addAttribute("pageTitle", "Cart");
        model.addAttribute("cartItems", cartService.getCart(session));
        model.addAttribute("cartTotal", cartService.getTotal(session));
        return "cart";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam Long productId,
                          @RequestParam(defaultValue = "1") int quantity,
                          HttpSession session,
                          RedirectAttributes ra) {
        cartService.addItem(session, productId, quantity);
        ra.addFlashAttribute("successMsg", "Added to cart!");
        return "redirect:/products/" + productId;
    }

    @PostMapping("/update")
    public String updateItem(@RequestParam Long productId,
                             @RequestParam int quantity,
                             HttpSession session) {
        cartService.updateQuantity(session, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam Long productId,
                             HttpSession session,
                             RedirectAttributes ra) {
        cartService.removeItem(session, productId);
        ra.addFlashAttribute("successMsg", "Item removed.");
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes ra) {
        cartService.clearCart(session);
        ra.addFlashAttribute("successMsg", "Cart cleared.");
        return "redirect:/cart";
    }
}
