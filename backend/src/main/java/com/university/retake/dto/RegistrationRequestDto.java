package com.university.retake.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegistrationRequestDto {
    @NotNull(message = "studentId không được trống")
    private UUID studentId;

    @NotNull(message = "courseSectionId không được trống")
    private UUID courseSectionId;

    @NotNull(message = "registrationPeriodId không được trống")
    private UUID registrationPeriodId;

    private String registrationType; // RETAKE | NEW | IMPROVE
    private String note;
}
