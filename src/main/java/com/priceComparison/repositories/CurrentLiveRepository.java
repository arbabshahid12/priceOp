package com.priceComparison.repositories;

import com.priceComparison.model.CurrentLiveProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentLiveRepository extends JpaRepository<CurrentLiveProduct,String> {

//    @Query("SELECT c FROM CurrentLiveProduct c WHERE c.productId = :productId")
    CurrentLiveProduct findByProductId(Long productId);
}
