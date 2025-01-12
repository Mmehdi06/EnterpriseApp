package be.ehb.enterpriseapp.controllers;

import be.ehb.enterpriseapp.models.CartItem;
import be.ehb.enterpriseapp.models.Product;
import be.ehb.enterpriseapp.models.User;
import be.ehb.enterpriseapp.repositories.ProductRepository;
import be.ehb.enterpriseapp.repositories.UserRepository;
import be.ehb.enterpriseapp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String redirectToUserCart() {
        // Fetch the logged-in user from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get username of the logged-in user

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return "redirect:/cart/user/" + user.getId();
    }


    @GetMapping("/user/{userId}")
    public String getCartItemsByUser(@PathVariable Long userId, Model model) {
        User user = userRepository.getReferenceById(userId);
        List<CartItem> cartItems = cartService.getCartItemsByUser(user);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PostMapping("/add")
    public String addCartItem(@ModelAttribute CartItem cartItem, @RequestParam Long productId, @RequestParam int quantity, Model model) {

        // Check if the product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (quantity > product.getQuantity()) {
            model.addAttribute("error", "Requested quantity exceeds available stock.");
            model.addAttribute("product", product);
            return "product_detail"; // Return to the product detail page with an error message
        }


        // Fetch the logged-in user from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get username of the logged-in user

        // Call the service to add the cart item
        cartService.addCartItem(cartItem, productId, username);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));


        return "redirect:/cart/user/" + user.getId(); // Adjust based on how you want to redirect
    }


    @DeleteMapping("/clear/{userId}")
    public String clearCart(@PathVariable Long userId) {
        User user = userRepository.getReferenceById(userId);
        cartService.clearCart(user);
        return "redirect:/cart/user/" + userId;
    }

    @PostMapping("/remove/{cartItemId}")
    public String removeCartItem(@PathVariable Long cartItemId, @RequestParam Long userId) {
        cartService.removeCartItem(cartItemId);
        return "redirect:/cart/user/" + userId;
    }

//    @PostMapping("/checkout")
//    public String checkout(Model model, @RequestParam Long userId) {
//        User user = userRepository.getReferenceById(userId);
//        cartService.checkout(user);
//        model.addAttribute("cartItems", cartService.getCartItemsByUser(user));
//        return "checkout";
//    }

    @PostMapping("/checkout")
    public String showCheckoutPage(@RequestParam Long userId, Model model) {
        User user = userRepository.getReferenceById(userId);
        List<CartItem> cartItems = cartService.getCartItemsByUser(user);

        double totalPrice = cartService.calculateTotalPrice(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("userId", userId);
        return "checkout"; // Return the checkout page
    }

    @PostMapping("/confirm")
    public String confirmOrder(@RequestParam Long userId) {
        User user = userRepository.getReferenceById(userId);
        cartService.checkout(user); // Process the checkout (clear cart, reduce stock)
        return "redirect:/cart/confirmation"; // Redirect to confirmation page
    }

    @GetMapping("/confirmation")
    public String showConfirmationPage() {
        return "confirmation"; // Return the confirmation page
    }

}

