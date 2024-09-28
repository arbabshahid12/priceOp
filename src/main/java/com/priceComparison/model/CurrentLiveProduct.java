package com.priceComparison.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Table(name = "live_productss")
//@Data
//@Entity
//public class CurrentLiveProduct {
//    @Id
//    @GeneratedValue (strategy = GenerationType.IDENTITY)
//    private String id;
//    private boolean isLive;
//    private Product products;
//}

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

    private Long productId;

}