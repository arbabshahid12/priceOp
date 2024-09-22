package com.priceComparison.services;

import com.priceComparison.model.PriceHistory;
import com.priceComparison.repositories.PriceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceHistoryService {
    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    public PriceHistory addPriceHistory(PriceHistory priceHistory) {
       return priceHistoryRepository.save(priceHistory);
    }
    public List<PriceHistory> getAllHistory(){
        return priceHistoryRepository.findAll();
    }

    public List<PriceHistory> getPriceHistory(Long productId) {
        return priceHistoryRepository.findByProductId(productId);
    }
}
