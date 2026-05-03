package com.university.retake.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GradeComponentDto {
    private String componentCode;
    private String componentName;
    private BigDecimal weightPercentage;
    private BigDecimal score;
}
