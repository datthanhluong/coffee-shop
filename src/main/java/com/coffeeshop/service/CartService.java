package com.coffeeshop.service;

import com.coffeeshop.dto.CartItemDTO;
import com.coffeeshop.entity.Product;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final String CART_KEY = "cart";

    private final ProductService productService;

    @SuppressWarnings("unchecked")
    public List<CartItemDTO> getCart(HttpSession session) {
        List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute(CART_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_KEY, cart);
        }
        return cart;
    }

    public void addItem(HttpSession session, Long productId, int quantity) {
        List<CartItemDTO> cart = getCart(session);
        Product product = productService.getById(productId);

        Optional<CartItemDTO> existing = cart.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            cart.add(CartItemDTO.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .imageUrl(product.getImageUrl())
                    .unitPrice(product.getPrice())
                    .quantity(quantity)
                    .build());
        }
        session.setAttribute(CART_KEY, cart);
    }

    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        List<CartItemDTO> cart = getCart(session);
        if (quantity <= 0) {
            removeItem(session, productId);
            return;
        }
        cart.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(quantity));
        session.setAttribute(CART_KEY, cart);
    }

    public void removeItem(HttpSession session, Long productId) {
        List<CartItemDTO> cart = getCart(session);
        cart.removeIf(i -> i.getProductId().equals(productId));
        session.setAttribute(CART_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_KEY);
    }

    public BigDecimal getTotal(HttpSession session) {
        return getCart(session).stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getItemCount(HttpSession session) {
        return getCart(session).stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();
    }
}
