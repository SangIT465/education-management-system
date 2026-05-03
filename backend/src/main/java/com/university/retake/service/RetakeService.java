package com.university.retake.service;

import com.university.retake.dto.*;
import com.university.retake.entity.*;
import com.university.retake.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service xử lý logic chính của Nhóm 6:
 * - Tính điểm tổng kết môn
 * - Xác định môn cần học lại (Failed)
 * - Lấy danh sách lớp học phần đang mở để đăng ký lại
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RetakeService {

    private final StudentRepository studentRepository;
    private final StudentCourseSectionRepository scsRepository;
    private final GradeComponentRepository gradeRepository;
    private final CourseSectionRepository sectionRepository;

    // Ngưỡng điểm pass/fail (theo thang 10)
    private static final BigDecimal PASS_THRESHOLD = new BigDecimal("5.0");
    // Ngưỡng điểm cải thiện (dưới ngưỡng này có thể đăng ký học cải thiện)
    private static final BigDecimal IMPROVE_THRESHOLD = new BigDecimal("7.0");

    /**
     * Lấy danh sách môn học cần học lại của sinh viên.
     * Logic:
     * 1. Lấy tất cả các môn đã hoàn thành (status = 'completed')
     * 2. Tính điểm tổng kết từ grade_components: SUM(score * weight / 100)
     * 3. Nếu điểm tổng < 5.0 -> môn FAILED -> cần học lại
     * 4. Lấy thêm các lớp học phần đang mở của môn đó để sinh viên chọn đăng ký
     */
    public List<RetakeCourseDto> getRetakeCoursesByStudent(UUID studentId) {
        // 1. Kiểm tra sinh viên có tồn tại
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + studentId));

        log.info("==> Lấy danh sách môn học lại cho sinh viên: {} ({})",
                student.getStudentCode(), student.getFullName());

        // 2. Lấy tất cả lịch sử các môn đã học completed
        List<StudentCourseSection> completedList = scsRepository.findCompletedByStudentId(studentId);
        log.info("==> Có {} môn đã hoàn thành", completedList.size());

        List<RetakeCourseDto> retakeCourses = new ArrayList<>();

        // 3. Lặp qua từng môn, tính điểm tổng kết
        for (StudentCourseSection scs : completedList) {
            List<GradeComponent> components = gradeRepository.findByStudentCourseSectionId(scs.getId());

            if (components.isEmpty()) {
                log.warn("Không có điểm thành phần cho học phần: {}", scs.getId());
                continue;
            }

            // Tính điểm tổng kết = SUM(score * weight / 100)
            BigDecimal totalScore = calculateTotalScore(components);

            // Xác định trạng thái pass/fail
            String gradeStatus = determineGradeStatus(totalScore);

            // Chỉ lấy các môn FAILED (rớt) vào danh sách học lại
            if (!"FAILED".equals(gradeStatus)) {
                continue;
            }

            CourseSection section = scs.getCourseSection();
            Course course = section.getCourse();
            Semester semester = section.getSemester();

            // 4. Lấy các lớp học phần đang mở của môn đó (để sinh viên chọn đăng ký lại)
            List<CourseSection> openSections = sectionRepository.findOpenSectionsByCourseId(course.getId());
            List<AvailableSectionDto> availableSections = openSections.stream()
                    .map(this::toAvailableSectionDto)
                    .collect(Collectors.toList());

            RetakeCourseDto dto = RetakeCourseDto.builder()
                    .studentCourseSectionId(scs.getId())
                    .courseId(course.getId())
                    .courseCode(course.getCode())
                    .courseName(course.getName())
                    .credits(course.getCredits())
                    .semesterName(semester.getName())
                    .semesterCode(semester.getCode())
                    .totalScore(totalScore)
                    .gradeStatus(gradeStatus)
                    .gradeComponents(components.stream()
                            .map(this::toGradeComponentDto)
                            .collect(Collectors.toList()))
                    .availableSections(availableSections)
                    .build();

            retakeCourses.add(dto);
        }

        log.info("==> Có {} môn cần học lại", retakeCourses.size());
        return retakeCourses;
    }

    /**
     * Tính điểm tổng kết: SUM(score * weight / 100)
     */
    private BigDecimal calculateTotalScore(List<GradeComponent> components) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (GradeComponent gc : components) {
            if (gc.getScore() == null || gc.getWeightPercentage() == null) continue;
            BigDecimal weight = gc.getWeightPercentage();
            BigDecimal contribution = gc.getScore()
                    .multiply(weight)
                    .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            total = total.add(contribution);
            totalWeight = totalWeight.add(weight);
        }

        // Nếu tổng trọng số chưa đạt 100%, điểm sẽ bị thiếu - không tính được tổng kết chính xác
        // ở đây ta vẫn trả về điểm theo phần đã có
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Xác định trạng thái:
     * - FAILED: < 5.0 (rớt - phải học lại)
     * - IMPROVABLE: 5.0 <= điểm < 7.0 (đậu nhưng có thể học cải thiện)
     * - PASSED: >= 7.0 (đậu tốt - không cần học lại)
     */
    private String determineGradeStatus(BigDecimal totalScore) {
        if (totalScore.compareTo(PASS_THRESHOLD) < 0) {
            return "FAILED";
        } else if (totalScore.compareTo(IMPROVE_THRESHOLD) < 0) {
            return "IMPROVABLE";
        } else {
            return "PASSED";
        }
    }

    private GradeComponentDto toGradeComponentDto(GradeComponent gc) {
        return GradeComponentDto.builder()
                .componentCode(gc.getComponentCode())
                .componentName(gc.getComponentName())
                .weightPercentage(gc.getWeightPercentage())
                .score(gc.getScore())
                .build();
    }

    private AvailableSectionDto toAvailableSectionDto(CourseSection cs) {
        Integer max = cs.getMaxStudents() == null ? 0 : cs.getMaxStudents();
        Integer cur = cs.getCurrentStudents() == null ? 0 : cs.getCurrentStudents();
        return AvailableSectionDto.builder()
                .sectionId(cs.getId())
                .sectionCode(cs.getCode())
                .sectionName(cs.getName())
                .classType(cs.getClassType())
                .maxStudents(max)
                .currentStudents(cur)
                .remainingSlots(max - cur)
                .status(cs.getStatus())
                .semesterName(cs.getSemester() != null ? cs.getSemester().getName() : null)
                .registrationStart(cs.getRegistrationStart())
                .registrationEnd(cs.getRegistrationEnd())
                .build();
    }
}
