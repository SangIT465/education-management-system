package com.university.retake.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "grade_components")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GradeComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_course_section_id", nullable = false)
    private StudentCourseSection studentCourseSection;

    @Column(name = "component_code", length = 20)
    private String componentCode;

    @Column(name = "component_name", length = 50)
    private String componentName;

    @Column(name = "weight_percentage", precision = 5, scale = 2)
    private BigDecimal weightPercentage;

    @Column(precision = 4, scale = 2)
    private BigDecimal score;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
