package com.coursework.clickboard.Models.Database.ShoppingCart;

import com.coursework.clickboard.Models.DTO.ShopCart.CartItemViewDTO;
import com.coursework.clickboard.Models.Database.Product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cartItems")
public class CartItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quantity")
    @NotNull
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private ShoppingCart cart;

    public CartItem(){
    }

    public CartItem(CartItemViewDTO cartItemViewDTO){
                this.product = new Product(cartItemViewDTO.getProduct());
                this.quantity = cartItemViewDTO.getQuantity();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public void reduceQuantity() {
        this.quantity--;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
}
