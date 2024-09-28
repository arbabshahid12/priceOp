package com.priceComparison.repositories;

import com.priceComparison.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {

//    Merchant findByEmail(String email);

    @Query("select m from Merchant m where m.userName =:username")
    Merchant getByUserName(@Param("username") String username);

    Merchant findByEmail(String email);

   boolean existsByEmail(String email);
}
