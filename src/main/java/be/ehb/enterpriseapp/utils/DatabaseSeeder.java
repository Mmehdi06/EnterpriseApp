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

import java.math.BigDecimal;
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
        User admin = new User( "admin", adminPassword, UserRole.ADMIN);
        User user = new User( "user", userPassword, UserRole.USER);
        userRepository.saveAll(Arrays.asList(admin, user));

        // Seed Categories and store them in a map for easy lookup
        Map<String, Category> categoryMap = new HashMap<>();
        categoryMap.put("Electronics", categoryRepository.save(new Category( "Electronics")));
        categoryMap.put("Clothing", categoryRepository.save(new Category( "Clothing")));
        categoryMap.put("Home Appliances", categoryRepository.save(new Category( "Home Appliances")));
        categoryMap.put("Books", categoryRepository.save(new Category("Books")));
        categoryMap.put("Sports", categoryRepository.save(new Category( "Sports & Outdoors")));

        // Seed Products using the saved categories
        productRepository.saveAll(List.of(
                new Product( "Smartphone", "A high-end smartphone with a great camera.", 699.99, 50, categoryMap.get("Electronics")),
                new Product( "Laptop", "A powerful laptop for work and gaming.", 999.99, 30, categoryMap.get("Electronics")),
                new Product( "4K TV", "A large 4K Ultra HD television.", 1299.99, 20, categoryMap.get("Electronics")),

                new Product( "T-shirt", "Comfortable cotton T-shirt.", 19.99, 100, categoryMap.get("Clothing")),
                new Product( "Jeans", "Stylish and durable denim jeans.", 49.99, 60, categoryMap.get("Clothing")),
                new Product( "Jacket", "Warm winter jacket.", 89.99, 40, categoryMap.get("Clothing")),

                new Product( "Blender", "A high-speed blender for smoothies.", 39.99, 25, categoryMap.get("Home Appliances")),
                new Product( "Microwave", "A compact microwave oven.", 199.99, 15, categoryMap.get("Home Appliances")),
                new Product( "Vacuum Cleaner", "A powerful vacuum cleaner.", 149.99, 10, categoryMap.get("Home Appliances")),

                new Product( "Fiction Novel", "An engaging fiction novel.", 14.99, 70, categoryMap.get("Books")),
                new Product( "Science Textbook", "A comprehensive science textbook.", 49.99, 50, categoryMap.get("Books")),
                new Product( "Cookbook", "A collection of delicious recipes.", 24.99, 40, categoryMap.get("Books")),

                new Product( "Football", "Standard size football.", 29.99, 80, categoryMap.get("Sports")),
                new Product( "Mountain Bike", "A durable mountain bike.", 499.99, 5, categoryMap.get("Sports")),
                new Product( "Yoga Mat", "A non-slip yoga mat.", 19.99, 100, categoryMap.get("Sports"))
        ));

        System.out.println("Database seeding completed!");
    }

    @Override
    public void run(String... args) throws Exception {
        seedDatabase();
    }
}

