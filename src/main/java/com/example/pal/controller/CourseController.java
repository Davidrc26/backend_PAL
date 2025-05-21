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

    //Busqueda de cursos gratuitos
    @GetMapping("/free")
    public ResponseEntity<List<CourseDTO>> getFreeCourses() {
        List<CourseDTO> freeCourses = courseService.getFreeCourses();
        return ResponseEntity.ok(freeCourses);
    }

    //Busqueda de cursos por categoria
    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<CourseDTO>> getCoursesByCategory(@PathVariable("category") String category) {
        List<CourseDTO> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(courses);
    }

    //Busqueda de cursos por palabra clave
    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> searchCourses(
        @RequestParam("q") String keyword,
        @RequestParam(value = "free", required = false) Boolean free,
        @RequestParam(value = "difficulty", required = false) String difficulty,
        @RequestParam(value = "minRating", required = false) Double minRating,
        @RequestParam(value = "orderBy", required = false, defaultValue = "relevance") String orderBy
        ) {
    List<CourseDTO> results = courseService.searchCourses(keyword, free, difficulty, minRating, orderBy);
    // List<CourseDTO> results = courseService.searchCourses(keyword);
    return ResponseEntity.ok(results);
}
}