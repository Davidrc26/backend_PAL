package com.example.pal.repository;

import java.util.Optional;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pal.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findById(long id);
    Optional<Course> findByTitle(String title);

    //Buscar cursos gratuitos
    @Query("SELECT c FROM Course c WHERE c.price = 0")
    List<Course> findByPrice(double price);

    //Buscar cursos por categoría.name
    @Query("SELECT c FROM Course c WHERE c.category.name = :category")
    List<Course> findByCategory(String category);

    //Buscar cursos por título, descripción o categoría
    // @Query("SELECT c FROM Course c WHERE " +
    //     "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    //     "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    //     "LOWER(c.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    // List<Course> searchCoursesByKeyword(String keyword);


    @Query("SELECT c FROM Course c WHERE " +
        "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(c.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchCoursesByKeyword(@Param("keyword") String keyword);
}
