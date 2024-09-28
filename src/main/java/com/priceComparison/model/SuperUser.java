package com.priceComparison.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "super_user")
@Getter
@Setter
@Builder
public class SuperUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

    // Add a no-argument constructor
    public SuperUser() {
    }

    // If using @Builder, add an all-args constructor
    @Builder
    public SuperUser(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }
}

