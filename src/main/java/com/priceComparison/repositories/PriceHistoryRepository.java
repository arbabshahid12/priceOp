package com.priceComparison.repositories;

import com.priceComparison.model.PriceHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByProductId(Long productId);
}
