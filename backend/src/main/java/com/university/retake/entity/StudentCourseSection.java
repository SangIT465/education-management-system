package com.university.retake.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_course_sections")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentCourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id", nullable = false)
    private CourseSection courseSection;

    @Column(length = 50)
    private String status; // studying, completed, dropped

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(length = 255)
    private String note;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
