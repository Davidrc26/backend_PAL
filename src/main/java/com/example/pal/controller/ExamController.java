package com.example.pal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pal.dto.CreateExamDTO;
import com.example.pal.dto.ExamDTO;
import com.example.pal.service.ExamService;


@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/create")
    public ResponseEntity<ExamDTO> createExam(@RequestBody CreateExamDTO examDTO) {
        ExamDTO newExam = examService.createExam(examDTO);
        return ResponseEntity.ok(newExam);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExamDTO>> getAllExams() {
        List<ExamDTO> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> getExamById(@PathVariable Long id) {
        ExamDTO exam = examService.getExamById(id);
        return ResponseEntity.ok(exam);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ExamDTO> updateExam(@PathVariable("id") Long id, @RequestBody CreateExamDTO examDTO) {
        ExamDTO updatedExam = examService.updateExam(id, examDTO);
        return ResponseEntity.ok(updatedExam);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable("id") Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
    
}
