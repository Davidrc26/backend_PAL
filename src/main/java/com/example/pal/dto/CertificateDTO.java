package com.example.pal.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CertificateDTO {
    private Long id;
    private String studentName;
    private String courseTitle;
    private LocalDateTime issuedAt;
    private String pdfUrl;
}