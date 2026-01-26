package com.example.academic_management_api.dto;

public class CategoryDto {
    private Integer categoryId;
    private String name;

    public CategoryDto(Integer categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }
}
