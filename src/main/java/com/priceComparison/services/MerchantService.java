package com.priceComparison.services;
import com.priceComparison.model.Merchant;
import com.priceComparison.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantService {
    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    public Merchant registerMerchant(Merchant merchant) {
        Merchant emialExit = merchantRepository.findByEmail(merchant.getEmail());
        System.out.println("mail exsit : " + emialExit);

        if(emialExit==null) {
            merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
            merchant.setEmail(merchant.getEmail());
            merchant.setRole(merchant.getRole());
            merchant = merchantRepository.save(merchant);
            merchant.setMessage("Create Account Successfully");
            merchant.setCode("200");
            return merchant;
        }
        merchant.setMessage("Email already exist");
        merchant.setCode("01");
        return merchant;
    }

    public Merchant findMerchantByEmail(String email) {

        return merchantRepository.findByEmail(email);
    }
}
