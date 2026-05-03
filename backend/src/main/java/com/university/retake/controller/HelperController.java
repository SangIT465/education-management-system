package com.university.retake.controller;

import com.university.retake.dto.ApiResponse;
import com.university.retake.entity.CourseSection;
import com.university.retake.entity.RegistrationPeriod;
import com.university.retake.repository.CourseSectionRepository;
import com.university.retake.repository.RegistrationPeriodRepository;
import com.university.retake.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller cho các API hỗ trợ: lấy danh sách sinh viên, đợt đăng ký, lớp học phần
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class HelperController {

    private final StudentRepository studentRepository;
    private final RegistrationPeriodRepository periodRepository;
    private final CourseSectionRepository sectionRepository;

    /**
     * API 5: Lấy danh sách sinh viên (để chọn ở giao diện)
     * GET /api/v1/students
     */
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllStudents() {
        List<Map<String, Object>> result = studentRepository.findAll().stream()
                .map(s -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", s.getId());
                    m.put("studentCode", s.getStudentCode());
                    m.put("fullName", s.getFullName());
                    m.put("className", s.getClassName());
                    m.put("email", s.getEmail());
                    return m;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * API 6: Lấy đợt đăng ký đang mở
     * GET /api/v1/registration-periods/open
     */
    @GetMapping("/registration-periods/open")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getOpenPeriods() {
        List<RegistrationPeriod> periods = periodRepository.findAllOpen();
        List<Map<String, Object>> result = periods.stream()
                .map(p -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", p.getId());
                    m.put("name", p.getName());
                    m.put("startTime", p.getStartTime());
                    m.put("endTime", p.getEndTime());
                    m.put("maxCredits", p.getMaxCredits());
                    m.put("minCredits", p.getMinCredits());
                    m.put("allowRetake", p.getAllowRetake());
                    m.put("semesterName", p.getSemester() != null ? p.getSemester().getName() : null);
                    return m;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * API 7: Lấy tất cả lớp học phần đang mở
     * GET /api/v1/course-sections/open
     */
    @GetMapping("/course-sections/open")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getOpenCourseSections() {
        List<CourseSection> sections = sectionRepository.findAllOpen();
        List<Map<String, Object>> result = sections.stream()
                .map(cs -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("sectionId", cs.getId());
                    m.put("sectionCode", cs.getCode());
                    m.put("sectionName", cs.getName());
                    m.put("classType", cs.getClassType());
                    m.put("maxStudents", cs.getMaxStudents());
                    m.put("currentStudents", cs.getCurrentStudents());
                    int remaining = (cs.getMaxStudents() == null ? 0 : cs.getMaxStudents())
                            - (cs.getCurrentStudents() == null ? 0 : cs.getCurrentStudents());
                    m.put("remainingSlots", remaining);
                    m.put("status", cs.getStatus());
                    m.put("courseId", cs.getCourse().getId());
                    m.put("courseCode", cs.getCourse().getCode());
                    m.put("courseName", cs.getCourse().getName());
                    m.put("credits", cs.getCourse().getCredits());
                    m.put("semesterName", cs.getSemester().getName());
                    m.put("registrationStart", cs.getRegistrationStart());
                    m.put("registrationEnd", cs.getRegistrationEnd());
                    return m;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
