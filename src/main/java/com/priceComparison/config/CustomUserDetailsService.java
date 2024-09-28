package com.priceComparison.config;

import com.priceComparison.model.Merchant;
import com.priceComparison.model.SuperUser;
import com.priceComparison.model.Users;
import com.priceComparison.repositories.MerchantRepository;
import com.priceComparison.repositories.SuperUserRepository;
import com.priceComparison.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SuperUserRepository superUserRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    //    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("email: "+email);
        SuperUser superUser = superUserRepository.findByEmail(email);
        System.out.println("superUser: "+superUser);
        if (superUser != null) {
            return User.builder()
                    .username(superUser.getEmail())
                    .password(superUser.getPassword()) // Password should be encoded with BCrypt
                    .roles("SUPERUSER")
                    .build();
        }

        Merchant merchant = merchantRepository.findByEmail(email);
        System.out.println("merchant: "+merchant);
        if (merchant != null) {
            return User.builder()
                    .username(merchant.getEmail())
                    .password(merchant.getPassword()) // Password should be encoded with BCrypt
                    .roles("MERCHANT")
                    .build();
        }

        Users user = userRepository.findByEmail(email);
        System.out.println("user: "+user);
        if (user != null) {
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword()) // Password should be encoded with BCrypt
                    .roles("User")
                    .build();
        }



        throw new UsernameNotFoundException("User not found");
    }
}
