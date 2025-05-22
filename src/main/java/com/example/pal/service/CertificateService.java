package com.example.pal.service;

import com.example.pal.dto.CertificateDTO;
import com.example.pal.enums.StateEnum;
import com.example.pal.model.Certificate;
import com.example.pal.model.Course;
import com.example.pal.model.User;
import com.example.pal.model.Exam;
import com.example.pal.model.ExamSubmission;
import com.example.pal.repository.CertificateRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.EnrollmentRepository;
import com.example.pal.repository.UserRepository;
import com.example.pal.repository.ExamSubmissionRepository;
import com.example.pal.repository.ExamRepository;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ExamSubmissionRepository examSubmissionRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public File generateCertificate(Long courseId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

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
                    .orElseThrow(
                            () -> new RuntimeException("El usuario no ha presentado el examen: " + exam.getTitle()));
            if (result.getScore() < 60) {
                throw new RuntimeException("El usuario no ha aprobado el examen: " + exam.getTitle());
            }
        }

        // Cambiar el estado del enrollment a COMPLETED
        enrollmentRepository.findByUserIdAndCourseId(userId, courseId).ifPresent(enrollment -> {
            enrollment.setState(StateEnum.COMPLETED);
            enrollmentRepository.save(enrollment);
        });

        Certificate certificate = new Certificate();
        certificate.setUser(user);
        certificate.setCourse(course);
        certificate.setIssuedAt(LocalDateTime.now());
        certificate.setPdfPath("certificates/certificate_" + user.getId() + "_" + course.getId() + ".pdf");

        Certificate saved = certificateRepository.save(certificate);

        // Generar el PDF del certificado
        File file = generateCertificatePdf(saved);

        CertificateDTO dto = new CertificateDTO();
        dto.setId(saved.getId());
        dto.setStudentName(user.getUsername()); // O user.getNombre() si tienes ese campo
        dto.setCourseTitle(course.getTitle());
        dto.setIssuedAt(saved.getIssuedAt());
        dto.setPdfUrl(saved.getPdfPath());

        return file;
    }

    private File generateCertificatePdf(Certificate certificate) {
        String pdfPath = certificate.getPdfPath();
        File file = new File(pdfPath);
        file.getParentFile().mkdirs(); // Crea la carpeta si no existe

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new java.io.FileOutputStream(pdfPath));
            document.open();

            // Logo (ajusta la ruta a tu logo real si lo deseas)
            // String logoPath = "src/main/resources/static/logo.png";
            // if (new File(logoPath).exists()) {
            //     Image logo = Image.getInstance(logoPath);
            //     logo.scaleToFit(100, 100);
            //     document.add(logo);
            // }

            document.add(new Paragraph("Certificado de Finalización"));
            document.add(new Paragraph("Otorgado a: " + certificate.getUser().getUsername()));
            document.add(new Paragraph("Por completar el curso: " + certificate.getCourse().getTitle()));
            document.add(new Paragraph("Fecha de emisión: " + certificate.getIssuedAt().toLocalDate().toString()));
            document.add(new Paragraph("Plataforma PAL"));

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF del certificado", e);
        }
        return file;
    }

    public Certificate getCertificate(Long certificateId) {
        return certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
    }
}