package com.example.pal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.pal.dto.CreateExamDTO;
import com.example.pal.dto.ExamDTO;
import com.example.pal.model.Course;
import com.example.pal.model.Exam;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.ExamRepository;



@Service
public class ExamService {
    
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ExamDTO createExam(CreateExamDTO examDTO) {
        Course course = courseRepository.findById(examDTO.getCourse()).orElseThrow(() -> new RuntimeException("Course not found"));
        
        Exam existingExam = examRepository.findByTitle(examDTO.getTitle()).orElse(null);
        if (existingExam != null) {
            // Si existe un examen con el mismo título, no se puede crear uno nuevo
            throw new RuntimeException("Ya existe un examen con ese nombre");
        }

        Exam newExam = modelMapper.map(examDTO, Exam.class);
        newExam.setTitle(examDTO.getTitle());
        newExam.setCourse(course);
        
        Exam savedExam = examRepository.save(newExam);
        return modelMapper.map(savedExam, ExamDTO.class);
    }

    public List<ExamDTO> getAllExams() {
        return examRepository.findAll().stream()
                .map(exam -> modelMapper.map(exam, ExamDTO.class))
                .collect(Collectors.toList());
    }

    public ExamDTO getExamById(Long id) {
        Exam exam = examRepository.findById(id).orElseThrow(() -> new RuntimeException("Exam not found!"));
        return modelMapper.map(exam, ExamDTO.class);
    }

    public ExamDTO updateExam(Long id, CreateExamDTO examDTO) {
        Exam existingExam = examRepository.findById(id).orElseThrow(() -> new RuntimeException("Exam not found!"));
        
        Course course = courseRepository.findById(examDTO.getCourse()).orElseThrow(() -> new RuntimeException("Course not found"));
        existingExam.setTitle(examDTO.getTitle());
        existingExam.setCourse(course);
        Exam updatedExam = examRepository.save(existingExam);
        return modelMapper.map(updatedExam, ExamDTO.class);
    }
    

    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id).orElseThrow(() -> new RuntimeException("Exam not found!"));
        examRepository.delete(exam);
    }
}
