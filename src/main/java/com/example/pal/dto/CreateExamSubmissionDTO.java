package com.example.pal.dto;

import lombok.Data;

@Data
public class CreateExamSubmissionDTO {
    private Long user;
    private Long exam;
    private Double score;
}