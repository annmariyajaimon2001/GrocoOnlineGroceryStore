package com.example.GrocoOnlineGroceryStore.controller;

import com.example.GrocoOnlineGroceryStore.entity.Category;
import com.example.GrocoOnlineGroceryStore.entity.Product;
import com.example.GrocoOnlineGroceryStore.entity.User;
import com.example.GrocoOnlineGroceryStore.service.CategoryService;
import com.example.GrocoOnlineGroceryStore.service.ProductService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    // Admin Home Page with Product Management Links

    @GetMapping("/admin-home")
    public String adminHome(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !loggedInUser.getRole().equals("admin")) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedInUser);
        // Retrieve all products from the database
        model.addAttribute("products", productService.getAllProducts());
        return "admin-home";
    }

    // Add Product Form
    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    // Process Add Product Form Submission
    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute Product product, @RequestParam("categoryName") String categoryName, Model model) {
        if (product.getName().isEmpty() || product.getPrice() == 0 ||  product.getImage().isEmpty()) {
            model.addAttribute("errorMessage", "All fields are required!");
            model.addAttribute("product", product);
            return "add-product";
        }
        // Retrieve or create the category based on the given name
        Category category = categoryService.findOrCreateCategory(categoryName);

        // Set the category of the product
        product.setCategory(category);

        // Save the product
        productService.addProduct(product);

        model.addAttribute("message", "Product added successfully!");
        return "redirect:/admin-home"; // Redirect to admin home page
    }


    // Edit Product Form
    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        //retrieves the product to be edited from the database based on the provided id
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "edit-product";
    }

    // Process Edit Product Form Submission
    @PostMapping("/edit-product/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product, Model model) {
        Product existingProduct = productService.getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());

        //Category category = categoryService.findOrCreateCategory(product.getCategory().getName());
        //existingProduct.setCategory(category);

        productService.updateProduct(existingProduct);

        model.addAttribute("message", "Product updated successfully!");
        return "redirect:/admin-home";
    }

    // Delete Product
    @PostMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) {
        productService.deleteProduct(id);
        model.addAttribute("message", "Product deleted successfully!");
        return "redirect:/admin-home"; // Redirect to admin home page
    }

    @GetMapping("/product/{productId}")
    public String getProductDetails(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            model.addAttribute("product", product);
            // You can access the associated category
            Category category = product.getCategory();
            if (category != null) {
                model.addAttribute("category", category);
            }
            return "product-details";
        } else {
            return "product-not-found";
        }
    }

}
