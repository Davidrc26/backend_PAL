package com.example.pal.controller;

import com.example.pal.dto.CourseDTO;
import com.example.pal.dto.CreateCourseDTO;
import com.example.pal.service.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CreateCourseDTO courseDTO) {
        CourseDTO newCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.ok(newCourse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable("id") Long id) {
        CourseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable("id") Long id, @RequestBody CreateCourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}