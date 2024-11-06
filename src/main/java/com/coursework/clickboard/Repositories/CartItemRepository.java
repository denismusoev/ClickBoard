package com.coursework.clickboard.Repositories;

import com.coursework.clickboard.Models.Database.ShoppingCart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
