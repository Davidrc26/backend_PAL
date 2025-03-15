package com.example.pal.controller;

import com.example.pal.dto.ContentDTO;
import com.example.pal.dto.CreateContentDTO;
import com.example.pal.model.Content;
import com.example.pal.service.ContentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @PostMapping
    public ResponseEntity<Content> createContent(@RequestBody CreateContentDTO contentDTO) {
        Content newContent = contentService.createContent(contentDTO);
        return ResponseEntity.ok(newContent);
    }

    @GetMapping
    public ResponseEntity<List<ContentDTO>> getAllContents() {
        List<ContentDTO> contents = contentService.getAllContents();
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContentById(@PathVariable Long id) {
        ContentDTO content = contentService.getContentById(id);
        return ResponseEntity.ok(content);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentDTO> updateContent(@PathVariable Long id, @RequestBody CreateContentDTO contentDTO) {
        ContentDTO updatedContent = contentService.updateContent(id, contentDTO);
        return ResponseEntity.ok(updatedContent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}