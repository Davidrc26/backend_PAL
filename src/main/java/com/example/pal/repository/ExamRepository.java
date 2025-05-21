package com.example.pal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.pal.model.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    Optional<Exam> findById(long id);
    Optional<Exam> findByTitle(String title);
}
