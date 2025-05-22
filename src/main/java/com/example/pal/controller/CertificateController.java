package com.example.pal.controller;

import com.example.pal.dto.CertificateDTO;
import com.example.pal.model.Certificate;
import com.example.pal.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/generate")
    public ResponseEntity<CertificateDTO> generateCertificate(@RequestParam("courseId") Long courseId, @RequestParam("userId") Long userId) {
        CertificateDTO dto = certificateService.generateCertificate(courseId, userId);
        return ResponseEntity.status(201).body(dto);
    }

    @GetMapping("/download/{certificateId}")
    public ResponseEntity<FileSystemResource> downloadCertificate(@PathVariable Long certificateId) {
        Certificate cert = certificateService.getCertificate(certificateId);
        FileSystemResource file = new FileSystemResource(cert.getPdfPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }
}