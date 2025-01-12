package be.ehb.enterpriseapp.repositories;

import be.ehb.enterpriseapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Product Repository
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

