package com.priceComparison.services;

import com.priceComparison.model.Product;
import com.priceComparison.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));
        product.setProductId(productDetails.getProductId());
        product.setProductName(productDetails.getProductName());
        product.setProductPrice(productDetails.getProductPrice());
        product.setBrandName(productDetails.getBrandName());
        product.setMerchantName(productDetails.getMerchantName());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));
        productRepository.delete(product);
    }
}
