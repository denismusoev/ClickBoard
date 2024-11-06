package com.coursework.clickboard.Repositories;

import com.coursework.clickboard.Models.Database.Product.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {
}
