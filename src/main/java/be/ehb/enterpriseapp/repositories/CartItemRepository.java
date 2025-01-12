package be.ehb.enterpriseapp.repositories;

import be.ehb.enterpriseapp.models.CartItem;
import be.ehb.enterpriseapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
}

