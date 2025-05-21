package com.example.pal.service;
import org.springframework.stereotype.Service;

import com.example.pal.dto.CreateQuestionDTO;
import com.example.pal.model.Question;
import com.example.pal.repository.QuestionRepository;

import com.example.pal.dto.QuestionDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import com.example.pal.model.Exam;
import com.example.pal.repository.ExamRepository;


@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExamRepository examRepository;

    public QuestionDTO createQuestion(CreateQuestionDTO questionDTO) {
        Exam exam = examRepository.findById(questionDTO.getExam()).orElseThrow(() -> new RuntimeException("Exam not found"));


        Question newQuestion = modelMapper.map(questionDTO, Question.class);
        newQuestion.setExam(exam);
        newQuestion.setContent(questionDTO.getContent());
        newQuestion.setAnswer(questionDTO.getAnswer());
        Question savedQuestion = questionRepository.save(newQuestion);
        return modelMapper.map(savedQuestion, QuestionDTO.class);
        
    }

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(question -> modelMapper.map(question, QuestionDTO.class))
                .collect(Collectors.toList());
    }

    public QuestionDTO getQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found!"));
        return modelMapper.map(question, QuestionDTO.class);
    }

    public QuestionDTO updateQuestion(Long id, CreateQuestionDTO questionDTO) {
        
        Question question = questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found!"));
        question.setContent(questionDTO.getContent());
        question.setAnswer(questionDTO.getAnswer());
        question.setExam(examRepository.findById(questionDTO.getExam()).orElseThrow(() -> new RuntimeException("Exam not found")));
        Question updatedQuestion = questionRepository.save(question);
        return modelMapper.map(updatedQuestion, QuestionDTO.class);
    }

    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found!"));
        questionRepository.delete(question);
    }

}
