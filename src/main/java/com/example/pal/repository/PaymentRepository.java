package com.example.pal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pal.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    @Query("SELECT p FROM Payment p WHERE p.course.id = :courseId AND p.user.id = :userId")
    Optional<Payment> findByCourseAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);
    
}
