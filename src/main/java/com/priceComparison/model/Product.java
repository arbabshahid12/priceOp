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

    private Long productId;
    private String productName;
    private String productPrice;
    private String name;
    private String brandName;
    private String merchantName;
    private String category;
    private String productType;

    public Product(Long id, Long productId, String productName, String productPrice, String name, String brandName, String merchantName, String category, String product) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.name = name;
        this.brandName = brandName;
        this.merchantName = merchantName;
        this.category = category;
        this.productType = product;
    }
//
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;

}
