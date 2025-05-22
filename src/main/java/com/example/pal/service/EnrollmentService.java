package com.example.pal.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.pal.dto.CreateEnrollmentDTO;
import com.example.pal.dto.EnrollmentDTO;
import com.example.pal.enums.StateEnum;
import com.example.pal.model.Course;
import com.example.pal.model.Enrollment;
import com.example.pal.model.User;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.EnrollmentRepository;
import com.example.pal.repository.PaymentRepository;
import com.example.pal.repository.UserRepository;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ModelMapper modelMapper;

    public EnrollmentDTO createEnrollment(CreateEnrollmentDTO enrollmentDTO) {

        Course course = courseRepository.findById(enrollmentDTO.getCourse())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid responsible id"));
        User user = userRepository.findById(enrollmentDTO.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user id"));
        enrollmentRepository.findByUserIdAndCourseId(course.getId(), user.getId()).ifPresent(enrollment -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already enrolled in this course");
        });

        if (course.getPrice() > 0.0) {
            paymentRepository.findByCourseAndUserId(course.getId(), user.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "User has not paid for this course"));
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setUser(user);
        enrollment.setState(StateEnum.IN_PROGRESS);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Bogota"));
        enrollment.setEnrollment_date(now);
        return modelMapper.map(enrollmentRepository.save(enrollment), EnrollmentDTO.class);
    }

    public List<EnrollmentDTO> getEnrollmentsByUser(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        return enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment, EnrollmentDTO.class))
                .toList();
    }
}
