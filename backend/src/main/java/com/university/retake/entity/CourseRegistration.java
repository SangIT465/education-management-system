package com.university.retake.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "course_registrations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id", nullable = false)
    private CourseSection courseSection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_period_id", nullable = false)
    private RegistrationPeriod registrationPeriod;

    @Column(name = "registration_type", length = 20)
    private String registrationType; // NEW, RETAKE, IMPROVE

    @Column(length = 20)
    private String status; // PENDING, APPROVED, REJECTED, CANCELED

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(length = 500)
    private String note;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @PrePersist
    public void prePersist() {
        if (registeredAt == null) registeredAt = LocalDateTime.now();
        if (status == null) status = "PENDING";
        if (registrationType == null) registrationType = "NEW";
        if (isActive == null) isActive = true;
    }
}
