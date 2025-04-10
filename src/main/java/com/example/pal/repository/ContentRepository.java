package com.example.pal.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pal.model.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findById(long id);

    //Buscar contenidos por curso, haciendo join a la tabla courses, busqueda por courseTitle
    @Query("SELECT c FROM Content c JOIN c.course co WHERE LOWER(co.title) = LOWER(:courseTitle)")
    List<Content> findContentsByCourseTitle(@Param("courseTitle") String courseTitle);
    
}
