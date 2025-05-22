package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

@Data
@Entity
@Table(name = "exam_submissions")
public class ExamSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Exam exam;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    @Convert(converter = MapToJsonConverter.class)
    private Map<Long, String> answers;
}