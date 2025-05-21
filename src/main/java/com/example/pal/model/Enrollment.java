package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "enrollments")
@IdClass(Enrollment.EnrollmentId.class)
public class Enrollment {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollment_date;

    
    @Data
    public static class EnrollmentId implements Serializable {
        private Long course;
        private Long user;
    }
}