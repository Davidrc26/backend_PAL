package com.example.pal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pal.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
       // Custom query to find enrollment by user ID and course ID (llave compuesta)
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.course.id = :courseId")
    Optional<Enrollment> findByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);

    // Custom query to find enrollments by course ID
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);

    
    List<Enrollment> findByUserId(Long userId);
}