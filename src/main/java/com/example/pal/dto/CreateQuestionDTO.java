package com.example.pal.dto;
import lombok.Data;

@Data
public class CreateQuestionDTO {
    
    
    private String content;
    private String answer;
    private Long exam;
}
