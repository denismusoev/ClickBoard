package com.coursework.clickboard.Models.DTO.Product;

import com.coursework.clickboard.Models.Database.Product.Product;

public class ProductNestedDTO {
    private int id;
    private String name;
    private String description;
    private float rating;
    private String image;

    public ProductNestedDTO() {
    }

    public ProductNestedDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.rating = product.getRating();
        this.image = product.getImage();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public String getImage() {
        return image;
    }
}
