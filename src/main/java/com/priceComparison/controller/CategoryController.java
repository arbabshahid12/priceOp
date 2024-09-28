package com.priceComparison.controller;

import com.priceComparison.model.Category;
import com.priceComparison.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity <List<Category>> getALlCategories() {
        List<Category>categories =categoryService.getAllCategories();


        return new  ResponseEntity<>(categories,HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
