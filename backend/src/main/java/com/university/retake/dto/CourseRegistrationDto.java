package com.university.retake.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseRegistrationDto {
    private UUID id;
    private UUID studentId;
    private String studentCode;
    private String studentName;
    private UUID courseSectionId;
    private String sectionCode;
    private String courseName;
    private Integer credits;
    private String registrationType;
    private String status;
    private LocalDateTime registeredAt;
    private String note;
}
