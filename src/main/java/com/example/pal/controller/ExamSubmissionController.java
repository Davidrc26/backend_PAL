package com.example.pal.controller;

import com.example.pal.dto.CreateExamSubmissionDTO;
import com.example.pal.model.Exam;
import com.example.pal.model.ExamSubmission;
import com.example.pal.model.User;
import com.example.pal.repository.ExamRepository;
import com.example.pal.repository.ExamSubmissionRepository;
import com.example.pal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam-submissions")
public class ExamSubmissionController {

    @Autowired
    private ExamSubmissionRepository examSubmissionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExamRepository examRepository;

    @PostMapping("/create")
    public ResponseEntity<ExamSubmission> createSubmission(@RequestBody CreateExamSubmissionDTO dto) {
        User user = userRepository.findById(dto.getUser())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Exam exam = examRepository.findById(dto.getExam())
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        ExamSubmission submission = new ExamSubmission();
        submission.setUser(user);
        submission.setExam(exam);
        submission.setScore(dto.getScore());

        ExamSubmission saved = examSubmissionRepository.save(submission);
        return ResponseEntity.status(201).body(saved);
    }
}