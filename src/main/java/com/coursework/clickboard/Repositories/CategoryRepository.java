package com.coursework.clickboard.Repositories;

import com.coursework.clickboard.Models.Database.Product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
