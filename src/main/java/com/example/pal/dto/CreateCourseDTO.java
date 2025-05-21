package com.example.pal.dto;

import lombok.Data;

@Data
public class CreateCourseDTO {
    private String title;
    private String description;
    private double price;
    private String difficulty;
    private double averageRating;
    private Long instructor;
    private Long category;
}
