package com.example.pal.controller;

import com.example.pal.dto.ContentDTO;
import com.example.pal.dto.CreateContentDTO;
import com.example.pal.model.Content;
import com.example.pal.service.ContentService;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @PostMapping("/create")
    public ResponseEntity<ContentDTO> createContent(@ModelAttribute CreateContentDTO contentDTO) {
        ContentDTO newContent = contentService.createContent(contentDTO);
        return ResponseEntity.ok(newContent);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContentDTO>> getAllContents() {
        List<ContentDTO> contents = contentService.getAllContents();
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContentById(@PathVariable Long id) {
        ContentDTO content = contentService.getContentById(id);
        return ResponseEntity.ok(content);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ContentDTO> updateContent(@PathVariable Long id, @ModelAttribute CreateContentDTO contentDTO) {
        ContentDTO updatedContent = contentService.updateContent(id, contentDTO);
        return ResponseEntity.ok(updatedContent);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download")
    public ResponseEntity<UrlResource> descargarArchivo(@RequestParam("name") String name) {
        try {
            Path ruta = Paths.get("uploads/" + name);
            UrlResource recurso = new UrlResource(ruta.toUri());
            String contentType = Files.probeContentType(ruta);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            if (recurso.exists() || recurso.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + name + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .body(recurso);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}