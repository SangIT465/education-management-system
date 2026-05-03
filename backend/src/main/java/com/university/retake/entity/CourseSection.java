package com.university.retake.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "course_sections")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String code;

    @Column(length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "max_students")
    private Integer maxStudents = 50;

    @Column(name = "min_students")
    private Integer minStudents = 10;

    @Column(name = "current_students")
    private Integer currentStudents = 0;

    @Column(name = "class_type", length = 255)
    private String classType; // theory / lab / hybrid / Lớp học lại

    @Column(length = 50)
    private String status; // planned / open / closed / canceled

    @Column(name = "registration_start")
    private LocalDateTime registrationStart;

    @Column(name = "registration_end")
    private LocalDateTime registrationEnd;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
