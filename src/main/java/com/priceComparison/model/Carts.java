package com.priceComparison.model;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Entity
@Table(name = "cart")
@Data
@Builder
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany
    private List<Product> products;
}
