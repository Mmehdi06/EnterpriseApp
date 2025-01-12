package be.ehb.enterpriseapp.controllers;

import be.ehb.enterpriseapp.models.Product;
import be.ehb.enterpriseapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String getAllProducts(@RequestParam(value = "category", required = false) String categoryName, Model model) {
        List<Product> products;

        if (categoryName != null && !categoryName.isEmpty()) {
            products = productService.getProductsByCategory(categoryName);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories()); // For displaying categories in the sidebar
        return "catalog";
    }


    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product_detail";
    }
}

