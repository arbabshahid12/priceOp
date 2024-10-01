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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/merchants")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

//    @Autowired
//    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<Merchant> registerMerchant(@RequestBody Merchant merchant, Authentication authentication) {
        String superUserId = ((User) authentication.getPrincipal()).getUsername();
        Merchant registeredMerchant = merchantService.registerMerchant(merchant, superUserId);
        return new ResponseEntity<>(registeredMerchant, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Merchant>> getAll() {
        List<Merchant> merchants = merchantService.getAllMerchants();
        return ResponseEntity.ok(merchants);
    }


    // Process login form
    @PostMapping("/login")
    public ResponseEntity<String> loginMerchant(@RequestBody Merchant merchant) {
        String res = merchantService.loginMerchant(merchant);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("addProductToMerchant/{id}")
    public ResponseEntity<Merchant> addProductToMerchant(@PathVariable(value = "id") String merchantId, @RequestBody Long productId) {
        return new ResponseEntity<>(merchantService.addProductToMerchant(merchantId, productId), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") String id){

        return new ResponseEntity<>(merchantService.deleteMerchant(id), HttpStatus.OK);
    }


}
