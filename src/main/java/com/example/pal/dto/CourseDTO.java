package com.example.pal.dto;


import java.util.Set;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private double price;
    private double averageRating;
    private String difficulty;
    private java.time.LocalDateTime createdAt;
    private UserDTO instructor;
    private CategoryDTO category;
    private Set<ContentDTO> contents;
}
