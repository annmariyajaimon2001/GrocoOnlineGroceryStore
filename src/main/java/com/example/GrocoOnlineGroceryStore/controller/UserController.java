package com.example.GrocoOnlineGroceryStore.controller;


import com.example.GrocoOnlineGroceryStore.entity.*;
import com.example.GrocoOnlineGroceryStore.service.CategoryService;
import com.example.GrocoOnlineGroceryStore.service.ProductService;
import com.example.GrocoOnlineGroceryStore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, @RequestParam("role") String role, Model model) {
        if (!isPasswordValid(user.getPassword())) {
            model.addAttribute("passwordError", "Password requirements not met.");
            return "register";
        }

        // Validate email format
        if (!isEmailValid(user.getEmail())) {
            model.addAttribute("emailError", "Email ID is not valid.");
            return "register";
        }
        // Check if user with the same email already exists
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            // Display appropriate message based on user's role
            if (existingUser.getRole().equals("admin")) {
                model.addAttribute("message", "Admin with this email already exists.");
            } else {
                model.addAttribute("message", "User with this email already exists.");
            }
            return "register";
        }

        user.setRole(role);
        userService.registerUser(user);
        model.addAttribute("user", new User());
        model.addAttribute("message", "User registered successfully!");
        return "register";
    }
    private boolean isPasswordValid(String password) {
        // Password should contain at least 8 characters, one letter, one number, and one special character
        return password.length() >= 8 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

    }

    private boolean isEmailValid(String email) {
        // Email should match a valid format
        return email.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginForm loginForm, Model model, HttpSession session) {
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();

        // Check if the user exists with the provided email and password
        User user = userService.login(email, password);

        if (user != null) {
            if (user.getRole().equals("admin")) {
                // Redirect to admin home if user is admin
                session.setAttribute("loggedInUser", user);
                return "redirect:/admin-home";
            } else {
                // Redirect to user home if user is not admin
                session.setAttribute("loggedInUser", user);
                return "redirect:/user-home";
            }
        } else {
            // User not found or invalid credentials
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }
    }




    @GetMapping("/user-home")
    public String userHome(Model model, HttpSession session) {
        //retrieves the user object stored in the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !loggedInUser.getRole().equals("user")) {
            return "redirect:/login";
        }
        // Fetch all products and categorize them for user home
        List<Category> categories = categoryService.getAllCategories();
        Map<Category, List<Product>> categorizedProducts = new HashMap<>();

        for (Category category : categories) {
            List<Product> products = productService.getProductsByCategory(category.getName());
            categorizedProducts.put(category, products);
        }
        List<Product> products = productService.getAllProducts();

        model.addAttribute("user", loggedInUser);
        model.addAttribute("categorizedProducts", categorizedProducts);
        model.addAttribute("products", products);

        return "user-home";
    }



    @GetMapping("/about")
    public String showAboutPage() {
        return "about"; // This resolves to about.html
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("passwordResetRequest", new PasswordResetRequest());
        return "forgot-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("passwordResetRequest") PasswordResetRequest passwordResetRequest,
                                BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the form with error messages
            return "forgot-password";
        }
        String email = passwordResetRequest.getEmail();
        String newPassword = passwordResetRequest.getNewPassword();
        String confirmPassword = passwordResetRequest.getConfirmPassword();

        // Validate input fields
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "forgot-password";
        }
        // Validate password requirements
        if (!isPasswordValid(newPassword)) {
            model.addAttribute("error", "Password requirements not met.");
            return "forgot-password";
        }

        // Check if the user with provided email exists
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Email not registered.");
            return "forgot-password";
        }


        // Update user's password
        user.setPassword(newPassword);
        userService.updateUser(user);

        model.addAttribute("message", "Password changed successfully. Please login with your new password.");
        return "login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/login";
    }

    @GetMapping("/usercart")
    public String showUserCart(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", loggedInUser);
        //retrieves the cart object stored in the session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            model.addAttribute("cart", null);
        } else {
            model.addAttribute("cart", cart);
            model.addAttribute("totalPrice", cart.getTotalPrice());
        }

        return "usercart";// Return usercart.html
    }

    @GetMapping("/vegetables")
    public String getVegetables(Model model) {
        List<Product> vegetables = productService.getProductsByCategory("vegetables");
        model.addAttribute("products", vegetables);
        return "vegetables"; // Renders vegetables.html
    }

    @GetMapping("/fruits")
    public String getFruits(Model model) {
        List<Product> fruits = productService.getProductsByCategory("fruits");
        model.addAttribute("products", fruits);
        return "fruits"; // Renders fruits.html
    }

    @GetMapping("/meat")
    public String getMeat(Model model) {
        List<Product> meat = productService.getProductsByCategory("meat");
        model.addAttribute("products", meat);
        return "meat"; // Renders meat.html
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam("address") String address,
                                  @RequestParam("phone") String phone,
                                  HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Cart cart = (Cart) session.getAttribute("cart");

        if (loggedInUser == null || cart == null || cart.getItems().isEmpty()) {
            return "redirect:/user-home"; // Redirect if no user is logged in or cart is empty
        }

        // Process the order
        // You can create an Order object and save it to the database here
        // Order order = new Order(loggedInUser, cart.getItems(), address, phone, "Cash on Delivery");
        // orderService.saveOrder(order);

        // Clear the cart after order is placed
        session.removeAttribute("cart");

        model.addAttribute("message", "Order has been placed successfully!");
        return "redirect:/user-home";
    }

}



