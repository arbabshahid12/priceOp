package com.priceComparison.repositories;

import com.priceComparison.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findByEmail(String email);

    @Query("select m from Merchant m where m.email =:email")
    Merchant getUserByUserName(@Param("email") String email);
}
