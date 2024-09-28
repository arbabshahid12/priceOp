package com.priceComparison.repositories;


import com.priceComparison.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
    Category findByName(String categoryName);

    List<Category>findByParentCategoryId(Long parentCategoryId);

}
