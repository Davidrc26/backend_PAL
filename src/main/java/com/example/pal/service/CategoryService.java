package com.example.pal.service;

import com.example.pal.dto.CategoryDTO;
import com.example.pal.model.Category;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pal.repository.CategoryRepository;
import java.util.Optional;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryDTO createCategory(String name) {
        Category newCategory = new Category();
        newCategory.setName(name);
        return modelMapper.map(categoryRepository.save(newCategory), CategoryDTO.class);
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
    }

    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(category -> modelMapper.map(category, CategoryDTO.class));
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDetails) {
        Category categoryData = modelMapper.map(categoryDetails, Category.class);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found!"));
        category.setName(categoryData.getName());
        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}
