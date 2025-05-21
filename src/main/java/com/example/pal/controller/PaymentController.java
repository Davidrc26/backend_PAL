package com.example.pal.controller;

import com.example.pal.dto.CreatePaymentDTO;
import com.example.pal.dto.PaymentDTO;
import com.example.pal.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody CreatePaymentDTO paymentDTO) {
        PaymentDTO created = paymentService.createPayment(paymentDTO);
        return ResponseEntity.status(201).body(created);
    }
}