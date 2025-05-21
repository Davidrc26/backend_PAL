package com.example.pal.service;

import com.example.pal.dto.CreatePaymentDTO;
import com.example.pal.dto.PaymentDTO;
import com.example.pal.model.Course;
import com.example.pal.model.Payment;
import com.example.pal.model.User;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.PaymentRepository;
import com.example.pal.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PaymentDTO createPayment(CreatePaymentDTO dto) {
        // Buscar usuario y curso
        User user = userRepository.findById(dto.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user id"));
        Course course = courseRepository.findById(dto.getCourse())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid course id"));

        if(course.getPrice() != dto.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid amount");
        }
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setCourse(course);
        payment.setAmount(dto.getAmount());
        payment.setPayment_date(LocalDateTime.now(ZoneId.of("America/Bogota")));

        Payment saved = paymentRepository.save(payment);
        return modelMapper.map(saved, PaymentDTO.class);
    }
}