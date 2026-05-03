package com.university.retake.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AvailableSectionDto {
    private UUID sectionId;
    private String sectionCode;
    private String sectionName;
    private String classType; // theory / lab / hybrid / Lớp học lại
    private Integer maxStudents;
    private Integer currentStudents;
    private Integer remainingSlots;
    private String status; // open / closed / planned
    private String semesterName;
    private LocalDateTime registrationStart;
    private LocalDateTime registrationEnd;
}
