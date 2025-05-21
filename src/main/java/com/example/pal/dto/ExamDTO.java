package com.example.pal.dto;
import java.util.Set;

import lombok.Data;

@Data
public class ExamDTO {
    private Long id;
    private String title;
    private Set<QuestionDTO> questions;
}
