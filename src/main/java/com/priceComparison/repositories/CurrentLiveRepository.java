package com.priceComparison.repositories;

import com.priceComparison.model.CurrentLiveProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentLiveRepository extends JpaRepository<CurrentLiveProduct,Long> {
    CurrentLiveProduct findByProductId(Long productId);
}
