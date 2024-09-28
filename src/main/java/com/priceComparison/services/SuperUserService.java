package com.priceComparison.services;

import com.priceComparison.model.Merchant;
import com.priceComparison.model.SuperUser;
import com.priceComparison.model.Users;
import com.priceComparison.repositories.MerchantRepository;
import com.priceComparison.repositories.SuperUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SuperUserService {

    @Autowired
    private SuperUserRepository superUserRepository;

    @Autowired
    private MerchantRepository merchantRepository;
//    private final BCryptPasswordEncoder passwordEncoder;

//    @Autowired
//    public SuperUserService(SuperUserRepository superUserRepository, BCryptPasswordEncoder passwordEncoder) {
//        this.superUserRepository = superUserRepository;
//        this.passwordEncoder = passwordEncoder;
//    }


    // Register a new super user
    public SuperUser registerUser(SuperUser superUser) {
        // Check if email is already in use
        if (superUserRepository.findByEmail(superUser.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }

        // Hash the password before saving
        superUser.setPassword(superUser.getPassword());

        // Set merchants to null
//        superUser.setMerchants(null);

        // Hash the password before saving
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(superUser.getPassword());
        superUser.setPassword(hashedPassword);

        System.out.println(superUser);
        // Save the super user to the database
        return superUserRepository.save(superUser);
    }

    // Log in a super user
    public String logInUser(SuperUser superUser) {
        SuperUser existingSuperuser = superUserRepository.findByEmail(superUser.getEmail());

        if (existingSuperuser == null) {
            System.out.println("in log in --existingSuperuser == null");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Verify the password using BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(superUser.getPassword(), existingSuperuser.getPassword())) {
            System.out.println("in log in password encoder");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // You can return a token or a success message here
        return "Login successful";
    }

//    public String logInUser(SuperUser superUser) {
//        SuperUser existingSuperuser = superUserRepository.findByEmail(superUser.getEmail());
//
//        if (existingSuperuser == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
//        }
//
//        if (!passwordEncoder.matches(superUser.getPassword(), existingSuperuser.getPassword())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
//        }
//
//        return "Login successful";
//    }
    // Retrieve all super users
    public List<SuperUser> getAllUsers() {
        return superUserRepository.findAll();
    }

    // Retrieve a super user by ID
    public SuperUser getUserById(Long id) {
        return superUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Super user not found"));
    }

    public String deleteUser(Long id) {
        superUserRepository.deleteById(id);
        return "deleted";
    }

    public Merchant createMerchant(Users user, String superUsername) {
        SuperUser superUser = superUserRepository.findByEmail(superUsername);
        System.out.println(superUser);
        Merchant merchant =new Merchant(user.getId(), user.getEmail(), user.getPassword(), user.getName(), null, superUser.getId());
        System.out.println(merchant);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        merchant.setPassword(hashedPassword);
        Merchant merchant1 = merchantRepository.save(merchant);
        System.out.println(merchant1);
        return merchant1;
    }


}
