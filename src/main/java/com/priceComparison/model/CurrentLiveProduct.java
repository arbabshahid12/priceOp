package com.priceComparison.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CurrentLiveProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private boolean isLive;

    @ManyToOne // or other appropriate relationship annotation
    @JoinColumn(name = "product_id")
    private Product product;

    public CurrentLiveProduct(Product product) {
        this.product = product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}