package com.example.pal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.pal.dto.CreateExamDTO;
import com.example.pal.dto.ExamDTO;
import com.example.pal.dto.ExamResultDTO;
import com.example.pal.model.Course;
import com.example.pal.model.Exam;
import com.example.pal.model.Question;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.EnrollmentRepository;
import com.example.pal.repository.ExamRepository;
import com.example.pal.dto.ExamSubmissionDTO;
import com.example.pal.model.ExamSubmission;
import com.example.pal.model.User;
import com.example.pal.repository.ExamSubmissionRepository;
import com.example.pal.repository.UserRepository;



@Service
public class ExamService {
    
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExamSubmissionRepository examSubmissionRepository;

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

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

    public Double submitExam(Long id, ExamSubmissionDTO examSubmissionDTO) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Examen no encontrado"));
        User user = UserRepository.findById(examSubmissionDTO.getUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int correctCount = 0;

        Map<Long, String> studentAnswers = examSubmissionDTO.getAnswers();

        for (Question q : exam.getQuestions()) {
            String studentAnswer = studentAnswers.getOrDefault(q.getId(), "");
            boolean isCorrect = q.getAnswer().equalsIgnoreCase(studentAnswer);

            if (isCorrect) correctCount++;
        }

        int total = exam.getQuestions().size();
        double score = Math.round((correctCount * 100.0) / total);

        // Guardar el envío
        ExamSubmission submit = new ExamSubmission();
        submit.setExam(exam);
        submit.setUser(user);
        submit.setAnswers(studentAnswers);
        submit.setScore(score);
        examSubmissionRepository.save(submit);

        // --- LÓGICA PARA CAMBIAR EL ESTADO DEL ENROLLMENT ---
        List<Exam> exams = examRepository.findAll().stream()
            .filter(e -> e.getCourse().getId().equals(exam.getCourse().getId()))
            .toList();

        boolean allPassed = true;
        for (Exam ex : exams) {
            ExamSubmission submission = examSubmissionRepository.findByUserIdAndExamId(user.getId(), ex.getId())
                .orElse(null);
            if (submission == null || submission.getScore() < 60) {
                allPassed = false;
                break;
            }
        }

        if (allPassed) {
            enrollmentRepository.findByUserIdAndCourseId(user.getId(), exam.getCourse().getId()).ifPresent(enrollment -> {
                enrollment.setState(com.example.pal.enums.StateEnum.COMPLETED);
                enrollmentRepository.save(enrollment);
            });
        }

        // --- FIN LÓGICA ---
        
        return score;
    }

    public ExamResultDTO getExamResultForStudent(Long examId, Long userId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Examen no encontrado"));

        ExamSubmission submit = examSubmissionRepository.findByUserIdAndExamId(userId, examId)
                .orElseThrow(() -> new RuntimeException("No se encontró un envío para este estudiante"));

        Map<Long, String> studentAnswers = submit.getAnswers();
        List<ExamResultDTO.QuestionResult> questionResults = new ArrayList<>();

        int correctCount = 0;

        for (Question q : exam.getQuestions()) {
            String studentAnswer = studentAnswers.getOrDefault(q.getId(), "");
            boolean isCorrect = q.getAnswer().equalsIgnoreCase(studentAnswer);

            if (isCorrect) correctCount++;

            ExamResultDTO.QuestionResult qr = new ExamResultDTO.QuestionResult();
            qr.setQuestion(q.getContent());
            qr.setCorrectAnswer(q.getAnswer());
            qr.setStudentAnswer(studentAnswer);
            qr.setCorrect(isCorrect);

            questionResults.add(qr);
        }

        Double score = (correctCount * 100.0) / exam.getQuestions().size();

        String feedback = (score >= 80) ? "¡Buen trabajo!" : (score >= 50) ? "Puedes mejorar" : "Necesitas estudiar más";

        ExamResultDTO result = new ExamResultDTO();
        result.setScore(score);
        result.setQuestionResults(questionResults);
        result.setFeedback(feedback);

        return result;
    }
}
