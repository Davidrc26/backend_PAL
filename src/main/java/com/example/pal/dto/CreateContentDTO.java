package com.example.pal.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateContentDTO {
    private MultipartFile file_url;
    private String type;
    private Long course;
}