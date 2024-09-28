package com.priceComparison.services;

import com.priceComparison.model.Merchant;
import com.priceComparison.model.Users;
import com.priceComparison.repositories.MerchantRepository;
import com.priceComparison.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MerchantRepository merchantRepository;


    public List<Users> getAll() {
        return userRepository.findAll();
    }

    public Users createNewUser(Users user) {
        if(user.getEmail().isEmpty() && user.getPassword().isEmpty() && user.getName().isEmpty()){
            throw new RuntimeException("Invalid User Data");
        }
        return userRepository.save(user);
    }

    public Users updateUser(Long userId, Users newUser) {
        Users user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User with Id "+userId+" not found"));
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());

        return user;
    }

    public String delete(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User with Id "+userId+" not found"));
        userRepository.delete(user);
        return "deleted";
    }
    public Merchant becomeMerchant(Long id, String email) {
        // Logic to create a Merchant based on the userId and email
        Merchant merchant = new Merchant();
        merchant.setId(id); // Assuming there's a field for user ID
        merchant.setEmail(email);
        // Other merchant initialization code...

        return merchantRepository.save(merchant); // Save the merchant to the database
    }



}
