package be.ehb.enterpriseapp.utils;

import be.ehb.enterpriseapp.models.User;
import be.ehb.enterpriseapp.models.UserRole;
import be.ehb.enterpriseapp.models.Category;
import be.ehb.enterpriseapp.models.Product;
import be.ehb.enterpriseapp.repositories.UserRepository;
import be.ehb.enterpriseapp.repositories.CategoryRepository;
import be.ehb.enterpriseapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seedDatabase() {
        // Check if data already exists
        if (userRepository.count() > 0 || categoryRepository.count() > 0 || productRepository.count() > 0) {
            System.out.println("Database already seeded, skipping seeding.");
            return;
        }

        // Encrypt passwords using BCryptPasswordEncoder
        String adminPassword = passwordEncoder.encode("admin123");
        String userPassword = passwordEncoder.encode("user123");

        // Seed Users
        User admin = new User("admin", adminPassword, UserRole.ADMIN);
        User user = new User("user", userPassword, UserRole.USER);
        userRepository.saveAll(Arrays.asList(admin, user));

        // Seed Categories and store them in a map for easy lookup
        Map<String, Category> categoryMap = new HashMap<>();
        categoryMap.put("Lighting", categoryRepository.save(new Category("Lighting")));
        categoryMap.put("Stage Elements", categoryRepository.save(new Category("Stage Elements")));
        categoryMap.put("Audio", categoryRepository.save(new Category("Audio")));
        categoryMap.put("Cables", categoryRepository.save(new Category("Cables")));
        categoryMap.put("Accessories", categoryRepository.save(new Category("Accessories")));

        // Seed Products using the saved categories
        productRepository.saveAll(List.of(
                // Lighting products
                new Product("LED Panel Light", "A bright LED panel for studio lighting.", 50.00, 10, categoryMap.get("Lighting")),
                new Product("Stage Spotlight", "A powerful spotlight for stage use.", 75.00, 5, categoryMap.get("Lighting")),

                // Stage elements
                new Product("Stage Platform", "Modular stage platform, 2x2 meters.", 100.00, 8, categoryMap.get("Stage Elements")),
                new Product("Truss System", "Aluminum truss for stage setup.", 150.00, 4, categoryMap.get("Stage Elements")),

                // Audio products
                new Product("Mixer Console", "16-channel audio mixer console.", 200.00, 2, categoryMap.get("Audio")),
                new Product("Speaker", "High-power speaker for events.", 120.00, 6, categoryMap.get("Audio")),
                new Product("Microphone", "Wireless microphone for performances.", 40.00, 20, categoryMap.get("Audio")),

                // Cables
                new Product("XLR Cable", "10-meter XLR audio cable.", 10.00, 50, categoryMap.get("Cables")),
                new Product("HDMI Cable", "5-meter HDMI cable for video.", 15.00, 30, categoryMap.get("Cables")),

                // Accessories
                new Product("Gaffer Tape", "Durable gaffer tape for stage setup.", 5.00, 100, categoryMap.get("Accessories")),
                new Product("Sandbag", "Weight sandbag for lighting stands.", 10.00, 25, categoryMap.get("Accessories"))
        ));

        System.out.println("Database seeding completed!");
    }

    @Override
    public void run(String... args) throws Exception {
        seedDatabase();
    }
}
