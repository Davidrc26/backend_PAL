package com.example.pal.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pal.dto.CategoryDTO;
import com.example.pal.service.CategoryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> postMethodName(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO categoryDto = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(201).body(categoryDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<CategoryDTO> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDetails){
    	CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDetails);
    	return ResponseEntity.ok(updatedCategory);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
    	categoryService.deleteCategory(id);
    	return ResponseEntity.noContent().build();
    }
}
