package com.coffeeshop.controller;

import com.coffeeshop.dto.CheckoutDTO;
import com.coffeeshop.entity.Order;
import com.coffeeshop.entity.User;
import com.coffeeshop.service.CartService;
import com.coffeeshop.service.OrderService;
import com.coffeeshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String showCheckout(Model model, HttpSession session,
                               Authentication auth) {
        if (cartService.getCart(session).isEmpty()) {
            return "redirect:/cart";
        }

        CheckoutDTO form = new CheckoutDTO();

        // Pre-fill form for logged-in users
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.getByEmail(auth.getName());
            form.setFullName(user.getFullName());
            form.setEmail(user.getEmail());
        }

        model.addAttribute("pageTitle", "Checkout");
        model.addAttribute("checkoutForm", form);
        model.addAttribute("cartItems", cartService.getCart(session));
        model.addAttribute("cartTotal", cartService.getTotal(session));
        return "checkout";
    }

    @PostMapping
    public String placeOrder(@Valid @ModelAttribute("checkoutForm") CheckoutDTO form,
                             BindingResult result,
                             HttpSession session,
                             Authentication auth,
                             Model model,
                             RedirectAttributes ra) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Checkout");
            model.addAttribute("cartItems", cartService.getCart(session));
            model.addAttribute("cartTotal", cartService.getTotal(session));
            return "checkout";
        }

        User user = (auth != null && auth.isAuthenticated())
                ? userService.getByEmail(auth.getName())
                : null;

        Order order = orderService.placeOrder(form, session, user);
        ra.addFlashAttribute("order", order);
        return "redirect:/checkout/confirmation/" + order.getId();
    }

    @GetMapping("/confirmation/{id}")
    public String confirmation(@PathVariable Long id, Model model) {
        Order order = orderService.getById(id);
        model.addAttribute("pageTitle", "Order Confirmed");
        model.addAttribute("order", order);
        return "order-confirmation";
    }
}
