package com.coffeeshop.controller.admin;

import com.coffeeshop.entity.Product;
import com.coffeeshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    private static final List<String> CATEGORIES =
            List.of("Cà phê", "Trà", "Chè", "Bánh");

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle",  "Manage Products");
        model.addAttribute("activePage", "admin");
        model.addAttribute("products",   productService.getAll());
        return "admin/products";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("pageTitle",  "New Product");
        model.addAttribute("activePage", "admin");
        model.addAttribute("product",    new Product());
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("formAction", "/admin/products");
        return "admin/product-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Product product,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle",  "New Product");
            model.addAttribute("activePage", "admin");
            model.addAttribute("categories", CATEGORIES);
            model.addAttribute("formAction", "/admin/products");
            return "admin/product-form";
        }
        productService.save(product);
        ra.addFlashAttribute("successMsg", "Product created successfully.");
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle",  "Edit Product");
        model.addAttribute("activePage", "admin");
        model.addAttribute("product",    productService.getById(id));
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("formAction", "/admin/products/" + id);
        return "admin/product-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Product product,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle",  "Edit Product");
            model.addAttribute("activePage", "admin");
            model.addAttribute("categories", CATEGORIES);
            model.addAttribute("formAction", "/admin/products/" + id);
            return "admin/product-form";
        }
        product.setId(id);
        productService.save(product);
        ra.addFlashAttribute("successMsg", "Product updated.");
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        productService.delete(id);
        ra.addFlashAttribute("successMsg", "Product deleted.");
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes ra) {
        Product p = productService.toggleAvailability(id);
        ra.addFlashAttribute("successMsg",
                p.getName() + " is now " + (p.isAvailable() ? "available" : "unavailable") + ".");
        return "redirect:/admin/products";
    }
}
