package com.coursework.clickboard.Repositories;

import com.coursework.clickboard.Models.Database.ShoppingCart.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
}
