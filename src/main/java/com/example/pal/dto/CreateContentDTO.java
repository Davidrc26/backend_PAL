package com.example.pal.dto;

import lombok.Data;

@Data
public class CreateContentDTO {
    private String file_url;
    private String type;
    private Long course;
}