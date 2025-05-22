package com.example.pal.dto;

import lombok.Data;

import java.util.Map;
import com.example.pal.model.Exam;
import com.example.pal.model.User;

@Data
public class ExamSubmissionDTO {
    private Long user;
    private Long exam;
    private Map<Long, String> answers; 
}