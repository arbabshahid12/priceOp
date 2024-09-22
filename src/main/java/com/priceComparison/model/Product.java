package com.priceComparison.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private String productName;
    private String productPrice;
    private String brandName;
    private String merchantName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
