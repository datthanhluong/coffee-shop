package com.coffeeshop.service;

import com.coffeeshop.dto.CartItemDTO;
import com.coffeeshop.dto.CheckoutDTO;
import com.coffeeshop.entity.Order;
import com.coffeeshop.entity.Order.OrderStatus;
import com.coffeeshop.entity.OrderItem;
import com.coffeeshop.entity.User;
import com.coffeeshop.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Transactional
    public Order placeOrder(CheckoutDTO form, HttpSession session, User user) {
        List<CartItemDTO> cartItems = cartService.getCart(session);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .guestName(user == null ? form.getFullName() : null)
                .guestEmail(user == null ? form.getEmail() : null)
                .guestPhone(form.getPhone())
                .deliveryAddress(form.getAddress())
                .notes(form.getNotes())
                .totalAmount(cartService.getTotal(session))
                .status(OrderStatus.PENDING)
                .build();

        List<OrderItem> items = cartItems.stream()
                .map(dto -> OrderItem.builder()
                        .order(order)
                        .product(productService.getById(dto.getProductId()))
                        .quantity(dto.getQuantity())
                        .unitPrice(dto.getUnitPrice())
                        .build())
                .toList();

        order.setItems(items);
        Order saved = orderRepository.save(order);
        cartService.clearCart(session);
        return saved;
    }

    public List<Order> getOrdersForUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Order not found: " + id));
    }

    // ── Admin ────────────────────────────────────────────

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order order = getById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
