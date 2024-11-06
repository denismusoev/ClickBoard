package com.coursework.clickboard.Models.DTO.Product;

import com.coursework.clickboard.Models.Database.Product.Category;

public class CategoryCompositeDTO {
    private int id;
    private String name;
    private String description;

    public CategoryCompositeDTO(){

    }

    public CategoryCompositeDTO(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
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
}
