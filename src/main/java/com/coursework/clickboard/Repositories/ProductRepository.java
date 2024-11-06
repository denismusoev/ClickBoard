package com.coursework.clickboard.Repositories;

import com.coursework.clickboard.Models.Database.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByCategory_Id(int categoryId, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> findByCategoryAndNameContainingIgnoreCase(@Param("categoryId") int categoryId, @Param("name") String name, Pageable pageable);
}

