package com.example.GrocoOnlineGroceryStore.controller;


import com.example.GrocoOnlineGroceryStore.entity.Cart;
import com.example.GrocoOnlineGroceryStore.entity.Product;
import com.example.GrocoOnlineGroceryStore.service.ProductService;
import jakarta.servlet.http.HttpSession;
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

    // Display products by category
    @GetMapping("/category/{categoryName}")
    public String showProductsByCategory(@PathVariable String categoryName, Model model) {
        List<Product> products = productService.getProductsByCategory(categoryName);
        model.addAttribute("products", products);
        return "products/category"; // Return the appropriate view (e.g., category.html)
    }
    @GetMapping("/all")
    public String showAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/all";
    }
    // Add to cart functionality

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") Long productId, @RequestParam("quantity") int quantity, HttpSession session) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);
            }
            cart.addItem(product, quantity);
            session.setAttribute("cart", cart); // Update the cart in the session
        }
        return "redirect:/user-home"; // Redirect to the cart page
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam("productId") Long productId, @RequestParam("quantity") int quantity, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.updateItemQuantity(productId, quantity);
            session.setAttribute("cart", cart); // Update the cart in the session
        }
        return "redirect:/usercart"; // Redirect to the cart page
    }

    @PostMapping("/delete-from-cart")
    public String deleteFromCart(@RequestParam("productId") Long productId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.removeItem(productId);
            session.setAttribute("cart", cart); // Update the cart in the session
        }
        return "redirect:/usercart"; // Redirect to the cart page
    }

}
