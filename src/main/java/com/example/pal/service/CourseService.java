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
        
        Course existingCourse = courseRepository.findByTitle(courseDTO.getTitle()).orElse(null);
        if (existingCourse != null) {
            // Si existe un curso con el mismo título, no se puede crear uno nuevo
            throw new RuntimeException("Ya existe un curso con ese nombre");
        }

        Course newCourse = modelMapper.map(courseDTO, Course.class);
        newCourse.setCategory(category);
        newCourse.setInstructor(instructor);
        newCourse.setCreatedAt(java.time.LocalDateTime.now());
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

    //Get all free courses
    public List<CourseDTO> getFreeCourses() {
        return courseRepository.findByPrice(0).stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }

    //Get all courses by category.name
    public List<CourseDTO> getCoursesByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(() -> new RuntimeException("Category not found"));
        return courseRepository.findByCategory(category.getName()).stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }

    //Get all courses by title, description or category
    // public List<CourseDTO> searchCourses(String keyword) {
    // List<Course> courses = courseRepository.searchCoursesByKeyword(keyword);
    // return courses.stream()
    //     .map(course -> modelMapper.map(course, CourseDTO.class))
    //     .collect(Collectors.toList());
    // }

    
    public List<CourseDTO> searchCourses(String keyword, Boolean free, String difficulty, Double minRating, String orderBy) {
        List<Course> courses = courseRepository.searchCoursesByKeyword(keyword);

        if (free != null) {
            if (free) {
                courses = courses.stream().filter(c -> c.getPrice() == 0).collect(Collectors.toList());
            } else {
                courses = courses.stream().filter(c -> c.getPrice() > 0).collect(Collectors.toList());
            }
        }

        if (difficulty != null && !difficulty.isEmpty()) {
            courses = courses.stream()
                .filter(c -> c.getDifficulty() != null && c.getDifficulty().equalsIgnoreCase(difficulty))
                .collect(Collectors.toList());
        }

        if (minRating != null) {
            courses = courses.stream()
                .filter(c -> c.getAverageRating() >= minRating)
                .collect(Collectors.toList());
        }

        if ("date".equalsIgnoreCase(orderBy)) {
            courses.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }

        return courses.stream()
            .map(course -> modelMapper.map(course, CourseDTO.class))
            .collect(Collectors.toList());
    }


}