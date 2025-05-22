package com.example.pal.service;

import com.example.pal.dto.CertificateDTO;
import com.example.pal.model.Certificate;
import com.example.pal.model.Course;
import com.example.pal.model.User;
import com.example.pal.model.Exam;
import com.example.pal.model.ExamSubmission;
import com.example.pal.repository.CertificateRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.UserRepository;
import com.example.pal.repository.ExamSubmissionRepository;
import com.example.pal.repository.ExamRepository;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;




@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ExamSubmissionRepository examSubmissionRepository;
    @Autowired
    private ExamRepository examRepository;


    // Aquí deberás validar que el usuario haya aprobado todos los exámenes del curso
    public CertificateDTO generateCertificate(Long courseId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        // Obtener todos los exámenes del curso
        List<Exam> exams = examRepository.findAll().stream()
            .filter(e -> e.getCourse().getId().equals(courseId))
            .toList();

        if (exams.isEmpty()) {
            
            throw new RuntimeException("El curso no tiene exámenes asociados");
        }

        // Validar que el usuario haya aprobado todos los exámenes
        for (Exam exam : exams) {
            ExamSubmission result = examSubmissionRepository.findByUserIdAndExamId(userId, exam.getId())
                .orElseThrow(() -> new RuntimeException("El usuario no ha presentado el examen: " + exam.getTitle()));
            if (result.getScore() < 60) { // Puedes ajustar el puntaje mínimo
                throw new RuntimeException("El usuario no ha aprobado el examen: " + exam.getTitle());
            }
        }
        

        Certificate certificate = new Certificate();
        certificate.setUser(user);
        certificate.setCourse(course);
        certificate.setIssuedAt(LocalDateTime.now());
        certificate.setPdfPath("certificates/certificate_" + user.getId() + "_" + course.getId() + ".pdf");

        Certificate saved = certificateRepository.save(certificate);
        return modelMapper.map(saved, CertificateDTO.class);
    }

    // Método para obtener el certificado y devolver el PDF
    public Certificate getCertificate(Long certificateId) {
        return certificateRepository.findById(certificateId).orElseThrow(() -> new RuntimeException("Certificate not found"));
    }
}