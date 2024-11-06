package com.coursework.clickboard.Models.DTO.ShopCart;

import com.coursework.clickboard.Models.Database.ShoppingCart.CartItem;
import com.coursework.clickboard.Models.DTO.Product.ProductViewDTO;

public class CartItemViewDTO {
    private ProductViewDTO product;
        private int quantity;

    public CartItemViewDTO(){

    }

    public CartItemViewDTO(CartItem cartItem) {
        this.product = new ProductViewDTO(cartItem.getProduct());
                this.quantity = cartItem.getQuantity();
    }


    public ProductViewDTO getProduct() {
        return product;
    }

    public void setProduct(ProductViewDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
