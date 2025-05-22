package com.example.pal.controller;

import com.example.pal.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
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
    public ResponseEntity<FileSystemResource> generateCertificate(@RequestParam("courseId") Long courseId,
            @RequestParam("userId") Long userId) {
        File pdfFile = certificateService.generateCertificate(courseId, userId);
        FileSystemResource resource = new FileSystemResource(pdfFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    // @GetMapping("/download/{certificateId}")
    // public ResponseEntity<FileSystemResource> downloadCertificate(@PathVariable
    // Long certificateId) {
    // Certificate cert = certificateService.getCertificate(certificateId);
    // FileSystemResource file = new FileSystemResource(cert.getPdfPath());
    // return ResponseEntity.ok()
    // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;
    // filename=certificate.pdf")
    // .contentType(MediaType.APPLICATION_PDF)
    // .body(file);
    // }
}