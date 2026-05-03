package com.university.retake.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO biểu diễn 1 môn cần học lại của sinh viên
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RetakeCourseDto {
    private UUID studentCourseSectionId;
    private UUID courseId;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private String semesterName;
    private String semesterCode;
    private BigDecimal totalScore; // Điểm tổng kết đã tính
    private String gradeStatus; // FAILED / PASSED / IMPROVABLE
    private List<GradeComponentDto> gradeComponents;
    // Danh sách lớp học phần đang mở để đăng ký lại
    private List<AvailableSectionDto> availableSections;
}
