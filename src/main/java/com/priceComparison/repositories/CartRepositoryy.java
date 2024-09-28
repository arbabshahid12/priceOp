package com.priceComparison.repositories;


import com.priceComparison.model.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepositoryy extends JpaRepository<Carts,Long> {
}
