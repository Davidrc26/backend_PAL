package com.example.pal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Content createContent(String type, String file_url, long course_id) {
        Course course = courseRepository.findById(course_id).orElseThrow(() -> new RuntimeException("Course not found!"));

        Content newContent = new Content();
        newContent.setType(type);
        newContent.setFile_url(file_url);
        newContent.setCourse(course);

        return contentRepository.save(newContent);
    }

    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    public Content getContentById(Long id) {
        return contentRepository.findById(id).orElseThrow(() -> new RuntimeException("Content not found!"));
    }

    public Content updateContent(Long id, Content contentDetails) {
        Content content = contentRepository.findById(id).orElseThrow(() -> new RuntimeException("Content not found!"));
        content.setType(contentDetails.getType());
        content.setFile_url(contentDetails.getFile_url());
        return contentRepository.save(content);
    }

    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }

}
