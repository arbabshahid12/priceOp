//package com.priceComparison.controller;
//
//
//import com.priceComparison.model.Merchant;
//import com.priceComparison.services.MerchantService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/merchant")
//public class MerchantController {
//    @Autowired
//    private MerchantService merchantService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
////    @PreAuthorize("hasRole('MERCHANT')")
//    @GetMapping("/getMerchant")
//    public List<Merchant> getMerchants() {
//        return merchantService.getAllMerchants();
//    }
//
////    @PreAuthorize("hasRole('MERCHANT')")
//    @PostMapping("/register")
//    public ResponseEntity<?> registerMerchant(@RequestBody Merchant merchant) {
//        Merchant registerMerchant = merchantService.registerMerchant(merchant);
//        return ResponseEntity.ok(registerMerchant);
//    }
//
//
//    //    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////    @PreAuthorize("hasRole('MERCHANT')")
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Merchant loginRequest) {
//        System.out.println("login " +loginRequest.getEmail());
//        Merchant foundMerchant = merchantService.findMerchantByEmail(loginRequest.getEmail());
//        if (foundMerchant == null) {
//            System.out.println("Merchant not found");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid email or password");
//        }
//
//        boolean passwordMatches=passwordEncoder.matches(loginRequest.getPassword(),foundMerchant.getPassword());
//        System.out.println("password matches  " + passwordMatches);
//        if (passwordMatches){
//            return ResponseEntity.status(HttpStatus.OK).body("Login SuccessFully");
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid password");
//    }
//}

package com.priceComparison.controller;

import com.priceComparison.model.Merchant;
import com.priceComparison.services.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/merchant")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Render the registration page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("merchant", new Merchant());
        return "merchant-register"; // Thymeleaf template for registration
    }

    // Process registration form
    @PostMapping("/register")
    public String registerMerchant(@ModelAttribute("merchant") Merchant merchant, Model model) {
        Merchant registeredMerchant = merchantService.registerMerchant(merchant);
        model.addAttribute("message", "Merchant registered successfully!");
        return "merchant-login";  // Redirect to login page after registration
    }

    // Render the login page
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("merchant", new Merchant());
        System.out.println("Login form");

        return "merchant-login"; // Thymeleaf template for login
    }

    // Process login form
    @PostMapping("/login")
    public String login(@ModelAttribute("merchant") Merchant loginRequest, Model model) {
        Merchant foundMerchant = merchantService.findMerchantByEmail(loginRequest.getEmail());

        System.out.println("email"+loginRequest.getEmail());
        System.out.println("password"+loginRequest.getPassword());

        if (foundMerchant == null || !passwordEncoder.matches(loginRequest.getPassword(), foundMerchant.getPassword())) {
            model.addAttribute("error", "Invalid email or password");
            return "merchant-login";
        }

        model.addAttribute("message", "Login Successful!");
        return "merchant-dashboard";  // Redirect to merchant dashboard after successful login
    }
}
