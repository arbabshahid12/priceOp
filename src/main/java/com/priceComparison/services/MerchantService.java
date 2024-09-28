package com.priceComparison.services;

import com.priceComparison.model.Merchant;
import com.priceComparison.repositories.MerchantRepository;
import com.priceComparison.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MerchantService {
    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    public Merchant registerMerchant(Merchant merchant) {
        Merchant emialExit = merchantRepository.findByEmail(merchant.getEmail());
        System.out.println("mail exsit : " + emialExit);

        if (emialExit == null) {
            merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
            merchant.setEmail(merchant.getEmail());
            merchant = merchantRepository.save(merchant);
//            merchant.setMessage("Create Account Successfully");
//            merchant.setCode("200");
            return merchant;
        }
//        merchant.setMessage("Email already exist");
//        merchant.setCode("01");
        return merchant;
    }

    public String loginMerchant(Merchant merchant) {
        System.out.println("merchant: " + merchant);
        Merchant existingMerchant = merchantRepository.findByEmail(merchant.getEmail());

        if (existingMerchant == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Verify the password using BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(merchant.getPassword(), existingMerchant.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // If login is successful, authenticate the user with Spring Security
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                existingMerchant.getEmail(), // Principal (email)
                null, // Credentials (password is not needed anymore)
                existingMerchant.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);


        System.out.println("login success");

        // You can return a token or a success message here
        return "Login successful";
    }

    public Merchant findMerchantByEmail(String email) {

        return merchantRepository.findByEmail(email);
    }

    public boolean isEmailRegistered(String email) {
        return merchantRepository.existsByEmail(email);
    }

    public Merchant addProductToMerchant(String merchantId, Long productId) {
        // Check if the category exists
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Merchant with ID '" + merchantId + "' does not exist"));

        System.out.println("merchant: " + merchant);
        System.out.println("productId: " + productId);
        // Check if the product exists
        boolean productExists = productRepository.existsById(productId);
        System.out.println(productExists);
        System.out.println(productRepository.findById(productId));
        if (!productExists) {
            throw new RuntimeException("Product with ID '" + productId + "' does not exist");
        }
        System.out.println("productExists: " + productExists);

        // Add productId to the category's productIds array
        List<Long> productIds = new ArrayList<>(Arrays.asList(merchant.getProductIds() != null ? merchant.getProductIds() : new Long[0]));
        if (!productIds.contains(productId)) {
            productIds.add(productId);
            merchant.setProductIds(productIds.toArray(new Long[0]));
        }


        // Save the updated category
        return merchantRepository.save(merchant);
    }

    public List<Merchant> getAll() {
        List<Merchant> merchants = merchantRepository.findAll();
        return merchants;
    }
}
