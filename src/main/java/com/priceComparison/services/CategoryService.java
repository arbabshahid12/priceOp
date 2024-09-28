package com.priceComparison.services;

import com.priceComparison.model.Category;
import com.priceComparison.model.Product;
import com.priceComparison.repositories.CategoryRepository;
import com.priceComparison.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        {
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new RuntimeException("Category name cannot be null or empty");
            }
            // Check if the parent category exists, if provided
            if (category.getParentCategoryId() != null) {
                boolean parentExists = categoryRepository.existsById(category.getParentCategoryId());
                if (!parentExists) {
                    throw new RuntimeException("Parent category with ID '" + category.getParentCategoryId() + "' does not exist");
                }
            }
            // If productIds array is not provided, set it to null
            if (category.getProductIds() == null) {
                category.setProductIds(null);
            } else {
                // Validate each productId in the array
                for (Long productId : category.getProductIds()){
                    boolean productExists=productRepository.existsById(productId);
                    if (!productExists) {
                        throw new RuntimeException("Product with ID '" + productId + "' does not exist");
                    }
                }
            }

            List<Category> existingCategories = categoryRepository.findAll();
            System.out.println(existingCategories);
            System.out.println(existingCategories);
            for (Category existingCategory : existingCategories) {
                if (existingCategory.getName().equalsIgnoreCase(category.getName())) {
                    throw new RuntimeException("Category with name '" + category.getName() + "' already exists");
                }
            }
            return categoryRepository.save(category);
        }
    }

    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Category with ID " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }
    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }
    public List<Category> getSubcategoriesByParentId(Long parentCategoryId) {
        return categoryRepository.findByParentCategoryId(parentCategoryId);
    }

    public List<Product> getProductsByCategory(Long id) {
        Optional<Category> categorys = categoryRepository.findById(id);
        System.out.println(categorys);
        List<Product> products = new ArrayList<>();

        // Check if the category exists
        if (categorys.isPresent()) {
            Category category = categorys.get();
            // Fetch and add products associated with this category
            products.addAll(getProductsFromCategory(category));

        }

        System.out.println(products.size());
        return products;
    }
    private List<Product> getProductsFromCategory(Category category) {
        List<Product> products = new ArrayList<>();
        Long[] productIds = category.getProductIds();

        if (productIds != null && productIds.length > 0) {
            for (Long productId : productIds) {
                System.out.println("productId: " + productId);
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product with ID '" + productId + "' does not exist"));
                System.out.println(product);
                products.add(product);
            }
        }
        return products;
    }
    public List<Category> getCategoryPath(Category category) {
        System.out.println("category.getParentCategoryId(): "+category.getParentCategoryId());
        List<Category> categoryPath = new ArrayList<>();

        // Traverse up the category tree to find all parent categories
        while (category != null) {
            categoryPath.add(category);
            Optional<Category> categorys = categoryRepository.findById(category.getParentCategoryId());
             category = categorys.get();
        }

        // Reverse to get path from root to current category
        Collections.reverse(categoryPath);
        return categoryPath;
    }
    public void deleteProductFromCategory(Long productID) {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            Long[] productIdsArray = category.getProductIds();
            System.out.println(productIdsArray);
            if (productIdsArray != null) { // Ensure it's not null
                List<Long> productIds = new ArrayList<>(Arrays.asList(productIdsArray));
                if (productIds.remove(productID)) {
                    // After modifying the list, update the category object
                    category.setProductIds(productIds.toArray(new Long[0]));
                    categoryRepository.save(category); // Save updated category
                    System.out.println("savign");
                }
            }
        }
    }

}

