package com.priceComparison.repositories;

import com.priceComparison.model.SuperUser;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface SuperUserRepository extends JpaRepository<SuperUser, Long> {
    SuperUser findByEmail(String email);
}
