package com.coffeeshop.controller.admin;

import com.coffeeshop.entity.Order.OrderStatus;
import com.coffeeshop.service.OrderService;
import com.coffeeshop.service.ProductService;
import com.coffeeshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        var allOrders   = orderService.getAllOrders();
        var pending     = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        var confirmed   = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.CONFIRMED).count();

        model.addAttribute("pageTitle",     "Admin Dashboard");
        model.addAttribute("activePage",    "admin");
        model.addAttribute("totalProducts", productService.getAll().size());
        model.addAttribute("totalOrders",   allOrders.size());
        model.addAttribute("totalUsers",    userRepository.count());
        model.addAttribute("pendingOrders", pending);
        model.addAttribute("confirmedOrders", confirmed);
        model.addAttribute("recentOrders",  allOrders.stream().limit(8).toList());
        return "admin/dashboard";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("pageTitle",  "Manage Orders");
        model.addAttribute("activePage", "admin");
        model.addAttribute("orders",     orderService.getAllOrders());
        model.addAttribute("statuses",   OrderStatus.values());
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam OrderStatus status) {
        orderService.updateStatus(id, status);
        return "redirect:/admin/orders";
    }
}
