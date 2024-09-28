package com.priceComparison.repositories;

import com.priceComparison.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken,String> {

   Optional <VerificationToken> findByToken(String token);

}
