package com.example.academic_management_api.dto;

import com.example.academic_management_api.entity.Courses;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CourseResponseDto {
    public static final String DEFAULT_THUMBNAIL =
            "http://localhost:8080/images/default-course.png";

    private Integer courseId;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDate createdAt;
    private String thumbnail;
    private InstructorDto instructor;
    private CategoryDto category;

    public CourseResponseDto(Integer courseId, String title, String description, BigDecimal price, String thumbnail, String instructorUsername, String instructorFullName, Integer categoryId, String categoryName) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
        this.thumbnail = thumbnail;
        this.instructor = new InstructorDto(instructorUsername, instructorFullName);
        this.category = new CategoryDto(categoryId, categoryName);
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public InstructorDto getInstructor() {
        return instructor;
    }

    public CategoryDto getCategory() {
        return category;
    }
}
