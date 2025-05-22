package com.example.pal.dto;
import lombok.Data;
import java.util.List;


@Data
public class ExamResultDTO {
    private Double score;
    private List<QuestionResult> questionResults;
    private String feedback;

    @Data
    public static class QuestionResult {
        private String question;
        private String correctAnswer;
        private String studentAnswer;
        private boolean isCorrect;
    }
}