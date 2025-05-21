package com.example.pal.dto;

import lombok.Data;

@Data
public class CreatePaymentDTO {
    private Long user;
    private Long course;
    private Double amount;
}
