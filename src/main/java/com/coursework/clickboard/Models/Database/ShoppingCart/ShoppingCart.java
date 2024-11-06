package com.coursework.clickboard.Models.Database.ShoppingCart;

import com.coursework.clickboard.Models.DTO.ShopCart.ShoppingCartDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "shoppingCarts")
public class ShoppingCart {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "cart_id")
    @OneToMany()
    private List<CartItem> cartItems;

    public ShoppingCart() {
    }

    public ShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        this.id = shoppingCartDTO.hashCode();
        this.cartItems = shoppingCartDTO.getCartItems().stream().map(CartItem::new).toList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void addToCartItems(CartItem item) {
        this.cartItems.add(item);
    }

    public void removeFromCartItems(CartItem cartItem) {
        this.cartItems.remove(cartItem);
    }

    public void increseProductQuantity(int productId) {
        this.cartItems.stream().filter(item -> item.getProduct().getId() == productId).findFirst().orElseThrow().increaseQuantity();
    }

    public int reduceProductQuantity(int productId) {
        CartItem cartItem = this.cartItems.stream().filter(item -> item.getProduct().getId() == productId).findFirst().orElseThrow();
        cartItem.reduceQuantity();
        return cartItem.getQuantity();
    }
}
