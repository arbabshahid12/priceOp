package com.priceComparison.services;

import com.priceComparison.model.PriceHistory;
import com.priceComparison.model.Product;
import com.priceComparison.repositories.PriceHistoryRepository;
import com.priceComparison.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    private CurrentLiveProductService currentLiveProductService;

    private PriceHistoryRepository priceHistoryRepository;

    private CategoryService categoryService;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product updateProduct(Product products) {
        Product product = productRepository.findById(products.getId())
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " +products));
        product.setProductId(products.getProductId());
        product.setProductName(products.getProductName());
        product.setProductPrice(products.getProductPrice());
        product.setBrandName(products.getBrandName());
        product.setMerchantName(products.getMerchantName());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));
        productRepository.delete(product);

    }
    public String deleteProductById(Long id) {
        // Find the product by ID, or throw an exception if not found
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        // Delete the product from the repository
        productRepository.delete(existingProduct);

        // Delete current live product associated with the product ID
        currentLiveProductService.deleteCurrentLiveProductByProductId(existingProduct.getId());

        // Print log messages for debugging
        System.out.println("before deletion: " + existingProduct.getId());

        // Remove the product from its associated category
        categoryService.deleteProductFromCategory(existingProduct.getId());

        System.out.println("deleted");
        return "Product deleted successfully";
    }


    public Product editProductById(Long id, Product productBody) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (!existingProduct.getProductPrice().equals(productBody.getProductPrice())) {
            PriceHistory priceHistory = new PriceHistory();
            priceHistory.setProductId(existingProduct.getId());
            priceHistory.setPrice(productBody.getProductPrice());
            priceHistory.setTimestamp(LocalDateTime.now());
            priceHistoryRepository.save(priceHistory);
        }
        validateProduct(productBody);

        existingProduct.setProductName(productBody.getProductName());
        existingProduct.setProductId(productBody.getProductId());
        existingProduct.setProductPrice(productBody.getProductPrice());
        existingProduct.setBrandName(productBody.getBrandName());
        existingProduct.setMerchantName(productBody.getMerchantName());

        return productRepository.save(existingProduct);
    }
    public List<PriceHistory> getPriceHistory(Long productId) {
        // Validate productId
        if (productId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product ID is required");
        }

        // Fetch price history
        return priceHistoryRepository.findByProductId(productId);
    }


    private void validateProduct(Product product) {
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product cannot be null");
        }
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
        }

    }    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }



}
