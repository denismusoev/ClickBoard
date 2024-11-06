package com.coursework.clickboard.Repositories;

import com.coursework.clickboard.Models.Database.Product.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
}

