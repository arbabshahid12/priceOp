package com.priceComparison.services;//package com.priceComparison.services;

import com.priceComparison.model.CurrentLiveProduct;
import com.priceComparison.repositories.CurrentLiveRepository;
import com.priceComparison.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentLiveProductService {

    @Autowired
    private CurrentLiveRepository currentLiveProductRepo;
    @Autowired
    private ProductRepository productRepository;

    public String changeLiveStatus(String id){
        CurrentLiveProduct currentLiveProduct = currentLiveProductRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Current live product not found"));

        if(currentLiveProduct.isLive()) {
            System.out.println("currentLiveProduct.isLive() before: " + currentLiveProduct.isLive());
            currentLiveProduct.setLive(false);
            currentLiveProductRepo.save(currentLiveProduct);
            System.out.println("currentLiveProduct.isLive() after: "+currentLiveProduct.isLive());
        }
        else{
            System.out.println("currentLiveProduct.isLive(): before " + currentLiveProduct.isLive());
            currentLiveProduct.setLive(true);
            currentLiveProductRepo.save(currentLiveProduct);
            System.out.println("currentLiveProduct.isLive(): after " + currentLiveProduct.isLive());

        }
        return "changed status";
    }

    public List<CurrentLiveProduct> getCurrentLiveProducts(){
        List<CurrentLiveProduct> currentLiveProducts = currentLiveProductRepo.findAll();
        if(currentLiveProducts.isEmpty()){
            throw new RuntimeException("No current live products found!");
        }
        List<CurrentLiveProduct> liveProducts = new ArrayList<>();
        for(CurrentLiveProduct currentLiveProduct : currentLiveProducts){
            if(currentLiveProduct.isLive()) {
                liveProducts.add(currentLiveProduct);
            }
        }
        return liveProducts;
    }

    public String deleteCurrentLiveProductByProductId(Long productId) {
        CurrentLiveProduct currentLiveProduct = currentLiveProductRepo.findByProductId(productId);
//                .orElseThrow(() -> new RuntimeException("Current live product with product ID " + productId + " not found"));
        currentLiveProductRepo.delete(currentLiveProduct);
        return "Current live product with product ID " + productId + " deleted successfully";
    }
}
