package be.ehb.enterpriseapp.services;

import be.ehb.enterpriseapp.models.Category;
import be.ehb.enterpriseapp.models.Product;
import be.ehb.enterpriseapp.repositories.CategoryRepository;
import be.ehb.enterpriseapp.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    /**
     * Returns all products from the database.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Finds a product by its ID.
     * @param id The ID of the product.
     * @return The product, or null if not found.
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Saves a new product or updates an existing one.
     * @param product The product to save.
     * @return The saved product.
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return productRepository.findByCategory(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}

