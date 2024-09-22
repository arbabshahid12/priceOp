package com.priceComparison.config;

import com.priceComparison.model.Merchant;
import com.priceComparison.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MerchantRepository merchantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Merchant merchant = merchantRepository.getUserByUserName(email);

        if (merchant==null){
            throw new UsernameNotFoundException("Could not find user");
        }
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + merchant.getRole()));

        return new org.springframework.security.core.userdetails.User(
                merchant.getEmail(),
                merchant.getPassword(),
                authorities
        );



//        GrantedAuthority authority = new SimpleGrantedAuthority(merchant.getRole());
////
//        return new org.springframework.security.core.userdetails.User(merchant.getEmail(), merchant.getPassword()
//                , AuthorityUtils.createAuthorityList(merchant.getRole()));

//        return new org.springframework.security.core.userdetails.User(
//                merchant.getEmail(),
//                merchant.getPassword(),
//                Collections.singletonList(authority)  // Return role as authority
//        );
    }
}
