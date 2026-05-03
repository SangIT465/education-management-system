package com.university.retake.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "registration_periods")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegistrationPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "target_config", columnDefinition = "nvarchar(max)")
    private String targetConfig;

    @Column(name = "max_credits")
    private Integer maxCredits = 25;

    @Column(name = "min_credits")
    private Integer minCredits = 12;

    @Column(name = "allow_retake")
    private Boolean allowRetake = true;

    @Column(name = "is_open")
    private Boolean isOpen = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
