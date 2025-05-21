package com.example.pal.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private Double amount;
    private LocalDateTime payment_date;
    private UserDTO user;
}
