package com.example.pal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pal.dto.ContentDTO;
import com.example.pal.dto.CreateContentDTO;
import com.example.pal.model.Content;
import com.example.pal.model.Course;
import com.example.pal.repository.ContentRepository;
import com.example.pal.repository.CourseRepository;

@Service
public class ContentService {
    @Autowired 
    private ContentRepository contentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Content createContent(CreateContentDTO contentDTO) {
        Course course = courseRepository.findById(contentDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        Content newContent = new Content();
        newContent.setType(contentDTO.getType());
        newContent.setFile_url(contentDTO.getFile_url());
        newContent.setCourse(course);

        return contentRepository.save(newContent);
    }

    public List<ContentDTO> getAllContents() {
        return contentRepository.findAll().stream().map(content -> modelMapper.map(content, ContentDTO.class)).collect(Collectors.toList());   
    }

    public ContentDTO getContentById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found!"));
        return modelMapper.map(content, ContentDTO.class);
    }

    public ContentDTO updateContent(Long id, CreateContentDTO contentDTO) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found!"));

        Course course = courseRepository.findById(contentDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        content.setType(contentDTO.getType());
        content.setFile_url(contentDTO.getFile_url());
        content.setCourse(course);

        return modelMapper.map(contentRepository.save(content), ContentDTO.class); 
    }

    public void deleteContent(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found!"));
        contentRepository.delete(content);
    }
}