package com.example.pal.controller;

import com.example.pal.dto.CreateEnrollmentDTO;
import com.example.pal.dto.EnrollmentDTO;
import com.example.pal.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/register")
    public ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody CreateEnrollmentDTO enrollmentDTO) {
        EnrollmentDTO created = enrollmentService.createEnrollment(enrollmentDTO);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/my-courses/{userId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByUser(@PathVariable("userId") Long userId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByUser(userId);
        return ResponseEntity.ok(enrollments);
    }
}