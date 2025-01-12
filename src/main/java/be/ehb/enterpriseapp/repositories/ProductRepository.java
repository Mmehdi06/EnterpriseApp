package be.ehb.enterpriseapp.repositories;

import be.ehb.enterpriseapp.models.Category;
import be.ehb.enterpriseapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Product Repository
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
}

