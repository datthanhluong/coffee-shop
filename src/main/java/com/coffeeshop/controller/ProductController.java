package com.coffeeshop.controller;

import com.coffeeshop.service.CartService;
import com.coffeeshop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/products")
    public String list(@RequestParam(required = false) String category,
                       @RequestParam(required = false) String q,
                       Model model, HttpSession session) {

        var products = (q != null && !q.isBlank())
                ? productService.search(q)
                : (category != null && !category.isBlank())
                    ? productService.getByCategory(category)
                    : productService.getAllAvailable();

        model.addAttribute("pageTitle", "Menu");
        model.addAttribute("activePage", "products");
        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("searchQuery", q);
        model.addAttribute("cartCount", cartService.getItemCount(session));
        model.addAttribute("categories", List.of("Cà phê", "Trà", "Chè", "Bánh"));
        return "products";
    }

    @GetMapping("/products/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        var product = productService.getById(id);
        model.addAttribute("pageTitle", product.getName());
        model.addAttribute("activePage", "products");
        model.addAttribute("product", product);
        model.addAttribute("cartCount", cartService.getItemCount(session));
        return "product-detail";
    }
}
