package be.ehb.enterpriseapp.controllers;

import be.ehb.enterpriseapp.models.Product;
import be.ehb.enterpriseapp.repositories.CategoryRepository;
import be.ehb.enterpriseapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Display admin page with products and stock management form
    @GetMapping("/products")
    public String showAdminPage(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin_products";
    }

    // Handle form submission to add a new product
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/admin/products?success=true";
    }

    // Handle stock update (increase or decrease stock)
    @PostMapping("/products/update-stock")
    public String updateStock(@RequestParam Long productId, @RequestParam int quantityChange) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int newQuantity = product.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            return "redirect:/admin/products?error=Insufficient stock"; // Prevent negative stock
        }

        product.setQuantity(newQuantity);
        productRepository.save(product);
        return "redirect:/admin/products?stockUpdated=true";
    }
}
