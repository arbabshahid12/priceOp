package com.priceComparison.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Entity
@Table(name = "merchants")
@Getter
@Setter
@ToString
public class Merchant implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String password;
    private String name;
    private String superUserId;

    private Long[] productIds;

    public Merchant(){

    }


    public Merchant(Long id, String userName, String email, String password, String name,String superUserId) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.name = name;
        this.superUserId = superUserId;
    }

    public Merchant(Long id, String email, String password, String name, String name1, Long id1) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("MERCHANT"));
    }

    @Override
    public String getUsername() {
        return "";
    }


//
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "merchant_role",
//            joinColumns = @JoinColumn(name = "merchant_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();


}
