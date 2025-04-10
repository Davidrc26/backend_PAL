package com.example.pal.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
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

    

}
