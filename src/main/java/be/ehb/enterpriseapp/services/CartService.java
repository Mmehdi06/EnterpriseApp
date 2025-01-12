package be.ehb.enterpriseapp.services;

import be.ehb.enterpriseapp.models.CartItem;
import be.ehb.enterpriseapp.models.Product;
import be.ehb.enterpriseapp.models.User;
import be.ehb.enterpriseapp.repositories.CartItemRepository;
import be.ehb.enterpriseapp.repositories.ProductRepository;
import be.ehb.enterpriseapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public List<CartItem> getCartItemsByUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void addCartItem(CartItem cartItem, Long productId, String username) {
        // Fetch the product from the database
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Fetch the user from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set the product and user in the cart item
        cartItem.setProduct(product);
        cartItem.setUser(user);

        // Save the cart item
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void checkout(User user) {
        List<CartItem> cartItems = getCartItemsByUser(user);

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int newQuantity = product.getQuantity() - cartItem.getQuantity();
            if (newQuantity < 0) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setQuantity(newQuantity);
            productRepository.save(product);
        }

        clearCart(user);
    }

    public void clearCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }

    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public double calculateTotalPrice(List<CartItem> cartItems) {
        // Calculate total price
        return cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
    }
}
