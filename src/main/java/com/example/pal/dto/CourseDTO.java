package com.example.pal.dto;
import java.util.Set;


import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private double price;
    private Set<ContentDTO> contents;

}
