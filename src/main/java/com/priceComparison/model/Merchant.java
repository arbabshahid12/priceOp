package com.priceComparison.model;

import jakarta.persistence.*;
import lombok.Data;

import javax.management.relation.Role;
import java.util.List;
import java.util.Set;

@Data

@Entity
@Table(name = "merchants")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String password;
    private Set<Role> role;
    private String message;
    private String code;

}
