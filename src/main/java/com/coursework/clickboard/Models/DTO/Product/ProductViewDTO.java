package com.coursework.clickboard.Models.DTO.Product;

import com.coursework.clickboard.Models.Database.Product.Product;

import java.util.List;

public class ProductViewDTO {
    private int id;
    private String name;
    private double price;
    private String description;
    private float rating;
    private String image;
    private CategoryCompositeDTO category;
    private List<AttributeNestedDTO> attributeList;

    public ProductViewDTO() {
    }

    public ProductViewDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.rating = product.getRating();
        this.image = product.getImage();
        this.category = new CategoryCompositeDTO(product.getCategory());
        this.attributeList = product.getProductAttributes().stream().map(AttributeNestedDTO::new).toList();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
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

    public CategoryCompositeDTO getCategory() {
        return category;
    }

    public List<AttributeNestedDTO> getAttributeList() {
        return attributeList;
    }
}
