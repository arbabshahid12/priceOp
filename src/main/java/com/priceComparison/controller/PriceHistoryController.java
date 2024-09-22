package com.priceComparison.controller;

import com.priceComparison.model.PriceHistory;
import com.priceComparison.services.PriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-history")
public class PriceHistoryController {
    @Autowired
    private PriceHistoryService priceHistoryService;

    @GetMapping("/getPriceHistory")
    public List<PriceHistory> getAllHistory(){
        return priceHistoryService.getAllHistory();
    }

    @PostMapping("/addPriceHistory")
    public PriceHistory addPriceHistory(@RequestBody PriceHistory priceHistory) {
        return priceHistoryService.addPriceHistory(priceHistory);
    }

    @GetMapping("/{productId}")
    public List<PriceHistory> getPriceHistory(@PathVariable Long productId) {
        return priceHistoryService.getPriceHistory(productId);
    }
}
