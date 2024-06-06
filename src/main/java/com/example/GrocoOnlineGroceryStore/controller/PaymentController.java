package com.example.GrocoOnlineGroceryStore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

    @GetMapping("/payment")
    public String showPaymentPage(Model model) {
        return "payment";
    }

    @PostMapping("/payment/submit")
    public String submitPayment(
            @RequestParam String deliveryAddress,
            @RequestParam String paymentMethod,
            Model model, HttpSession session) {
        // Handle the payment submission logic here
        // For now, we can just redirect to a confirmation page
        session.setAttribute("paymentSuccess", true);
        model.addAttribute("deliveryAddress", deliveryAddress);
        model.addAttribute("paymentMethod", paymentMethod);
        session.removeAttribute("cart");
        return "paymentSuccess";
    }
    @GetMapping("/payment/success")
    public String showPaymentSuccessPage(Model model, HttpSession session) {
        // Check if payment was successful
        Boolean paymentSuccess = (Boolean) session.getAttribute("paymentSuccess");

        if (paymentSuccess == null || !paymentSuccess) {
            // Redirect to some error page or handle the case where payment wasn't successful
            return "redirect:/error";
        }



        // Retrieve details from session and pass to the view
        String deliveryAddress = (String) session.getAttribute("deliveryAddress");
        String paymentMethod = (String) session.getAttribute("paymentMethod");

        model.addAttribute("deliveryAddress", deliveryAddress);
        model.addAttribute("paymentMethod", paymentMethod);

        // Clear session attributes related to payment
        session.removeAttribute("deliveryAddress");
        session.removeAttribute("paymentMethod");
        session.removeAttribute("paymentSuccess");

        return "paymentSuccess";
    }
}
