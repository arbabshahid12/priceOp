package com.priceComparison.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  // MongoDB uses String for the ID by default

    private String token;

    private Long merchantId;  // Store user ID instead of embedding User directly

    private Date expiryDate;

}
