package com.coffeeshop.config;

import com.coffeeshop.entity.Product;
import com.coffeeshop.entity.User;
import com.coffeeshop.entity.User.Role;
import com.coffeeshop.repository.ProductRepository;
import com.coffeeshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedProducts();
    }

    private void seedAdmin() {
        if (userRepository.existsByEmail("admin123@gmail.com")) return;

        // Xoá admin cũ nếu tồn tại
        userRepository.findByEmail("admin@brewbrute.com")
                .ifPresent(userRepository::delete);

        userRepository.save(User.builder()
                .fullName("Quản trị viên")
                .email("admin123@gmail.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build());

        System.out.println(">>> [LENGKENG] Admin đã tạo: admin123@gmail.com / admin123");
    }

    private void seedProducts() {
        if (productRepository.count() > 0) return;

        productRepository.saveAll(List.of(
            Product.builder()
                .name("Espresso Thuần Túy")
                .description("Espresso đậm đà, nguyên chất. Double shot từ hạt rang đậm đặc trưng với lớp crema dày và hậu vị mạnh mẽ không thể quên.")
                .price(new BigDecimal("35000"))
                .category("Cà phê")
                .available(true).featured(true).build(),

            Product.builder()
                .name("Latte Brutalist")
                .description("Sữa hấp mịn màng kết hợp cùng double espresso. Béo ngậy, đậm đà và cực kỳ dễ ghiền trong từng ngụm.")
                .price(new BigDecimal("55000"))
                .category("Cà phê")
                .available(true).featured(true).build(),

            Product.builder()
                .name("Cold Brew Nitơ")
                .description("Cold brew ủ 24 tiếng được bơm nitơ tạo lớp bọt mịn như kem. Không cần đá, uống thẳng cực đã.")
                .price(new BigDecimal("60000"))
                .category("Cà phê")
                .available(true).featured(true).build(),

            Product.builder()
                .name("Cappuccino Đặc Biệt")
                .description("Tỉ lệ vàng giữa espresso, sữa hấp và bọt sữa — phủ thêm một lớp cacao. Không màu mè, chỉ có hương vị.")
                .price(new BigDecimal("45000"))
                .category("Cà phê")
                .available(true).featured(false).build(),

            Product.builder()
                .name("Mocha Đen")
                .description("Socola đen đậm gặp double espresso với chút sữa hấp để cân bằng. Mạnh mẽ và cuốn hút đến giọt cuối.")
                .price(new BigDecimal("55000"))
                .category("Cà phê")
                .available(true).featured(true).build(),

            Product.builder()
                .name("Matcha Sữa Yến Mạch")
                .description("Matcha ceremonial grade đánh bông cùng sữa yến mạch. Thanh mát, béo nhẹ và khó cưỡng một cách đáng ngạc nhiên.")
                .price(new BigDecimal("50000"))
                .category("Trà")
                .available(true).featured(false).build(),

            Product.builder()
                .name("Americano")
                .description("Espresso pha loãng với nước nóng đến đầy ly. Sạch vị, mạnh mẽ, không rườm rà — đúng cho dân ghiền cà phê thật.")
                .price(new BigDecimal("35000"))
                .category("Cà phê")
                .available(true).featured(false).build(),

            Product.builder()
                .name("Cold Brew Mật Ong")
                .description("Cold brew mượt mà ủ 24 tiếng, ngọt tự nhiên từ mật ong nguyên chất và một chút muối biển. Mùa hè trong ly.")
                .price(new BigDecimal("65000"))
                .category("Cà phê")
                .available(true).featured(false).build()
        ));

        System.out.println(">>> Đã tạo 8 sản phẩm mẫu.");
    }
}
