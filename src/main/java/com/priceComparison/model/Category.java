package com.priceComparison.model;

import jakarta.persistence.*;
import lombok.Data;

@Data

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long parentCategoryId;
    private Long[] productIds;
}
