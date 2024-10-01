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
import java.util.regex.Pattern;

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
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    public Merchant registerMerchant(Merchant merchant, String superuserId) {
        validateMerchantData(merchant);

        if (merchantRepository.existsByEmail(merchant.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Validate product IDs
        if (merchant.getProductIds() != null && merchant.getProductIds().length > 0) {
            for (Long productId : merchant.getProductIds()) {
                boolean productExists = productRepository.existsById(productId);
                if (!productExists) {
                    throw new RuntimeException("Product with ID '" + productId + "' does not exist or is not available");
                }
            }
        }
        merchant.setSuperUserId(superuserId);

        // Hash the password before saving
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(merchant.getPassword());
        merchant.setPassword(hashedPassword);
        return merchantRepository.save(merchant);
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
    public String deleteMerchant(String id){
        merchantRepository.deleteById(id);
        return "deleted";
    }

    public List<Merchant> getAll() {
        List<Merchant> merchants = merchantRepository.findAll();
        return merchants;
    }
    private void validateMerchantData(Merchant merchant) {
        if (merchant.getEmail() == null || !Pattern.matches(EMAIL_REGEX, merchant.getEmail())) {
            throw new RuntimeException("Invalid email format");
        }

        if (merchant.getPassword() == null || merchant.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }

        if (merchant.getName() == null || merchant.getName().trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }
    }
}
