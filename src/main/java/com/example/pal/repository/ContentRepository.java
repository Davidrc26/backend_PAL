package com.example.pal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pal.model.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findById(long id);
}
