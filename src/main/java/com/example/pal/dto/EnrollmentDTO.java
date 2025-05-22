package com.example.pal.dto;

import java.time.LocalDateTime;


import lombok.Data;

@Data
public class EnrollmentDTO {
    long id;
    UserDTO user;
    CourseDTO course;
    LocalDateTime enrollment_date;
    String state;
}
