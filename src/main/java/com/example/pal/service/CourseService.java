package com.example.pal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pal.dto.CourseDTO;
import com.example.pal.dto.CreateCourseDTO;
import com.example.pal.model.Category;
import com.example.pal.model.Course;
import com.example.pal.model.User;
import com.example.pal.repository.CategoryRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.UserRepository;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CourseDTO createCourse(CreateCourseDTO courseDTO) {
        User instructor = userRepository.findById(courseDTO.getInstructor()).orElseThrow(() -> new RuntimeException("Instructor not found"));
        Category category = categoryRepository.findById(courseDTO.getCategory()).orElseThrow(() -> new RuntimeException("Category not found"));
        Course newCourse = modelMapper.map(courseDTO, Course.class);
        newCourse.setCategory(category);
        newCourse.setInstructor(instructor);
        Course savedCourse = courseRepository.save(newCourse);
        return modelMapper.map(savedCourse, CourseDTO.class);
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found!"));
        return modelMapper.map(course, CourseDTO.class);
    }

    //¿Como vamos a actualizar los contenidos de un curso?
    public CourseDTO updateCourse(Long id, CreateCourseDTO courseDTO) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found!"));
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setPrice(courseDTO.getPrice());
        User instructor = userRepository.findById(courseDTO.getInstructor()).orElseThrow(() -> new RuntimeException("Instructor not found"));
        Category category = categoryRepository.findById(courseDTO.getCategory()).orElseThrow(() -> new RuntimeException("Category not found"));
        course.setCategory(category);
        course.setInstructor(instructor);
        Course updatedCourse = courseRepository.save(course);
        return modelMapper.map(updatedCourse, CourseDTO.class);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}