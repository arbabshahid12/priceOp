package com.priceComparison.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.priceComparison.model.*;
import com.priceComparison.repositories.MerchantRepository;
import com.priceComparison.repositories.SuperUserRepository;
import com.priceComparison.repositories.TokenRepository;
import com.priceComparison.repositories.UserRepository;
import com.priceComparison.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductService productService;

    private final CategoryService categoryService;
    private final CurrentLiveProductService currentLiveProductService;
    //    private final MainProductService mainProductService;
    @Autowired
    private final MerchantService merchantService;
    @Autowired
    private UserService userService;
    @Autowired
    private SuperUserService superUserService;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private HttpSession httpSession;

    private SecurityContextHolder securityContextHolder;

    @Autowired
    private TokenRepository verificationTokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SuperUserRepository superUserRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String showProducts(Model model) {
        List<Product> products = productService.getAll();
        List<Category> categories = categoryService.getAllCategories();

        List<CurrentLiveProduct> liveProducts = currentLiveProductService.getCurrentLiveProducts();
        List<Product> mainProducts = productService.getAll();

        model.addAttribute("mainProducts", mainProducts);
        model.addAttribute("currentLiveProducts", liveProducts);
        System.out.println(categories);
        model.addAttribute("categories", categories);

//
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/auth")
    public String showAuthPage(@RequestParam(value = "role", required = false) String role,
                               @RequestParam(value = "action", required = false) String action, Model model) {
        boolean isSignUp = "signup".equals(action);
        boolean isMerchant = "merchant".equals(role);

        model.addAttribute("isSignUp", isSignUp);
        model.addAttribute("isMerchant", isMerchant);

        return "LoginorSignUp"; // Assuming the Thymeleaf file is named auth.html
    }

    @GetMapping("/category/{id}")
    public String getProductsByCategory(@PathVariable("id") Long categoryId, Model model) {
//        boolean selectedCat = true;
        Optional<Category> category1 = categoryService.getCategoryById(categoryId);
        Category category = category1.get();
        System.out.println(category);
        List<Product> products = categoryService.getProductsByCategory(categoryId);
        System.out.println(products);
        List<Category> subcategories = categoryService.getSubcategoriesByParentId(categoryId);
        System.out.println(subcategories);
        List<Category> categories = categoryService.getAllCategories(); // null for top-level categories

        System.out.println("categories");
        System.out.println(categories);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);  // Add selected category ID to the model
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        model.addAttribute("subcategories", subcategories);

        System.out.println("size: " + products.size());
        System.out.println(products);
        return "Product-v2";  // This is the view that will display products by category
    }

    @GetMapping("/product-details/{id}")
    public String showProductDetails(@PathVariable("id") Long id, Model model) throws JsonProcessingException {
        Product product = productService.getProductById(id);
        System.out.println(product);


        List<PriceHistory> priceHistory = productService.getPriceHistory(id);
        System.out.println(priceHistory);


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule for proper date serialization

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String priceHistoryJson = mapper.writeValueAsString(priceHistory);
        System.out.println(priceHistoryJson);
        String lowestPriceEver = null;
        String lowestPriceToday = null;
        LocalDateTime lowestPriceEverDate = null;

// Get today's date
        LocalDateTime today = LocalDateTime.now();

// Iterate through the price history to find the lowest prices
        for (PriceHistory history : priceHistory) {
            // Convert the price string to BigDecimal for comparison
            BigDecimal price = new BigDecimal(history.getPrice());

            // Update the lowest price ever
            if (lowestPriceEver == null || price.compareTo(new BigDecimal(lowestPriceEver)) < 0) {
                lowestPriceEver = history.getPrice(); // Store the price as a string
                lowestPriceEverDate = history.getTimestamp();    // Store the date
            }

            // Update the lowest price today (if the date matches today)
            if (lowestPriceEverDate.equals(today)) {
                if (lowestPriceToday == null || price.compareTo(new BigDecimal(lowestPriceToday)) < 0) {
                    lowestPriceToday = history.getPrice(); // Store the price as a string
                }
            }
        }

        // Fallback in case there's no price change today
        if (lowestPriceToday == null && !priceHistory.isEmpty()) {
            lowestPriceToday = priceHistory.get(priceHistory.size() - 1).getPrice();
        }

        // Now you can use lowestPriceEver, lowestPriceEverDate, and lowestPriceToday in your model

        String productCategoryId = product.getCategory();
        System.out.println("productCategoryId: " + productCategoryId);
        Category productCategory = categoryService.getCategoryByName(productCategoryId);
        System.out.println("productCategory: " + productCategory);
        List<Category> categoryPath = categoryService.getCategoryPath(productCategory);
        System.out.println("categoryPath: " + categoryPath);


        model.addAttribute("categoryPath", categoryPath);


        model.addAttribute("product", product);
        model.addAttribute("priceHistorys", priceHistoryJson);
        model.addAttribute("lowestPriceEver", lowestPriceEver);
        model.addAttribute("lowestPriceEverDate", lowestPriceEverDate);
        model.addAttribute("lowestPriceToday", lowestPriceToday);

        // Return the view name
        return "product-details";
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
//        logger.debug("Fetching all categories");
        List<Category> categories = categoryService.getAllCategories();

        // Separate main categories and subcategories
        List<Category> mainCategories = categories.stream()
                .filter(category -> category.getParentCategoryId() == null)
                .collect(Collectors.toList());

        Map<Long, List<Category>> subcategoriesMap = categories.stream()
                .filter(category -> category.getParentCategoryId() != null)
                .collect(Collectors.groupingBy(Category::getParentCategoryId));
        System.out.println(mainCategories);
        System.out.println(subcategoriesMap);
        model.addAttribute("mainCategories", mainCategories);
        model.addAttribute("subcategoriesMap", subcategoriesMap);
        model.addAttribute("categories", categories);

        return "category";
    }

    @GetMapping("/about-us")
    public String aboutUs(Model model) {
        return "Content";
    }

    @GetMapping("/live_products")
    public String liveProducts(Model model) {
        List<CurrentLiveProduct> liveProducts = currentLiveProductService.getCurrentLiveProducts();
        model.addAttribute("products", liveProducts);
        return "live_products";
    }

    @GetMapping("/upload")
    public String showUploadPage() {
        return "merchant_dashboard";  // Refers to uploadDatafeed.html (without the .html extension)
    }

    @GetMapping("/uploadDatafeed")
    public String uploadDatafeed() {
        return "uploadDatafeed";
    }

    @GetMapping("/main_products")
    public String mainProducts(Model model) {
        List<Product> mainProducts = productService.getAll();
        model.addAttribute("products", mainProducts);
        return "main_products";
    }

    @GetMapping("/loginSignup")
    public String signUp(Model model, Authentication authentication) {
        authentication = securityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        System.out.println(webAuthenticationDetails.getSessionId());
        if (authentication != null && authentication.isAuthenticated() && webAuthenticationDetails.getSessionId() != null) {
            System.out.println(authentication.isAuthenticated() + " principal-> " + authentication.getPrincipal());
            System.out.println("in");
            return "redirect:/merchant_section";  // Redirect if already authenticated
        }
        model.addAttribute("merchant", new Merchant());
        return "login_signup";
    }


    @PostMapping("/register")
    public String registerMerchant(@ModelAttribute Merchant merchant, Model model, RedirectAttributes redirectAttributes) {
        if (merchantService.isEmailRegistered(merchant.getEmail())) {
            System.out.println(merchantService.isEmailRegistered(merchant.getEmail()));
            System.out.println("Email is already registered.");
            model.addAttribute("errorMessage", "Email is already registered.");

            return "login_signup";
        }
        SuperUser superUser = superUserService.getAllUsers().get(0);
        String superusername = superUser.getEmail();
        Users user = Users.builder()
                .email(merchant.getEmail())
                .name(merchant.getName())
                .password(merchant.getPassword())
                .build();
        Users user1 = userService.createNewUser(user);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setMerchantId(user1.getId());
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 24 hours expiry

        verificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(merchant.getEmail(), token);
     model.addAttribute("successMessage", "Verification email sent to " + merchant.getEmail());


     return "login_signup"; // Redirect to login after successful registration
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) throws Exception {
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);

        if (optionalToken.isPresent()) {
            VerificationToken verificationToken = optionalToken.get();
            Long merchantId = verificationToken.getMerchantId();
            Users user = userRepository.findById(merchantId).orElseThrow(() -> new RuntimeException("Merchant not found"));
//            User user = verificationToken.getMerchantId();

            // Check if token has expired
            if (verificationToken.getExpiryDate().before(new Date())) {
                model.addAttribute("errorMessage", "Token has expired. Please register again.");
                return "login_signup"; // Token expired page
            }

            List<SuperUser> superUsers = superUserService.getAllUsers();

            Merchant merchant = userService.becomeMerchant(user.getId(), superUsers.get(0).getEmail());
            // Delete the token as it is no longer needed
            verificationTokenRepository.delete(verificationToken);

            model.addAttribute("merchant", merchant); // Make sure to add this line
            model.addAttribute("verified", true); // Add verified flag

            return "login_signup"; // Redirect to login page
        } else {
            model.addAttribute("errorMessage", "Invalid token.");
            return "login_signup"; // Invalid token page
        }
    }


    @PostMapping("/customlogin")
    public String loginMerchant(@ModelAttribute Merchant merchant, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("merchant email: " + merchant.getEmail());
            System.out.println("merchant password: " + merchant.getPassword());

            HttpSession session = request.getSession(true); // 'false' means don't create a new session if one doesn't exist

            merchantService.loginMerchant(merchant);
            System.out.println("success");


            System.out.println(SecurityContextHolder.getContext().getAuthentication());

            redirectAttributes.addFlashAttribute("successMessage", "Login successful");
            System.out.println("Success Message: " + redirectAttributes.getFlashAttributes().get("successMessage"));


            return "redirect:/merchant_section";// Redirect to a protected page after successful login

        } catch (ResponseStatusException e) {
//            model.addAttribute("errorMessage", Objects.requireNonNull(e.getReason()));
            model.addAttribute("errorMessage", (e.getReason()));
            System.out.println("Error Message: " + model.getAttribute("errorMessage"));

            System.out.println(e.getReason());
            return "login_signup"; // Return to login/signup page with error message
//            return "redirect:/loginSignup";
        }
    }

    @GetMapping("/merchant_section")
    public String merchantDashboard(Model model) {
        List<Product> products = productService.getAll();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        System.out.println("size: " + products.size());
//        System.out.println(products);
        if (products.isEmpty()) {
            return "EmptyPage"; // Render a different page if there are no products
        }
        return "merchant_dashboard";
    }

    @GetMapping("/success-page")
    public String successPage() {
        return "success_page"; // Name of the Thymeleaf template for the add product form
    }

    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
//        model.addAttribute("product", );
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        System.out.println("in add-product get");
        return "add_product"; // Name of the Thymeleaf template for the add product form
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute Product product) {
        System.out.println(product);
        // Assuming ProductService handles the business logic
        productService.addProduct(product);
        System.out.println("in add-product post");
        return "redirect:/merchant_section";  // Redirect after successful submission
    }

    @PostMapping("/update-product")
    public String updateProduct(@ModelAttribute Product product) {
        System.out.println(product);
        productService.updateProduct(product); // Your service to handle the update
        return "redirect:/merchant_section"; // Redirect back to product list
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id) {
        System.out.println(id);
        productService.deleteProductById(id);
        System.out.println("deleted product: " + id);
        return "redirect:/merchant_section";  // Redirect after deletion
    }

    @PostMapping("/add-category")
    public String addCategory(@ModelAttribute Category category) {
        System.out.println(category);
        if (category.getParentCategoryId() != null && category.getParentCategoryId().equals("null")) {
            category.setParentCategoryId(null); // Set the parent to null if "None" is selected
        }
        // Assuming ProductService handles the business logic
        categoryService.createCategory(category);
//        productService.addProduct(product);
        return "redirect:/categories";  // Redirect after successful submission
    }

    @GetMapping("/clogout")
    public String logout(HttpServletRequest request, Authentication authentication) {
//        authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.setAuthenticated(false);


        securityContextHolder.clearContext();

        // Invalidate the HTTP session and create a new one
        HttpSession session = request.getSession(false); // 'false' means don't create a new session if one doesn't exist
        if (session != null) {
            session.invalidate();  // Invalidate the current session
            System.out.println("Session invalidated");
        }
        return "redirect:/loginSignup";
    }

}
