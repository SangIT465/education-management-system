package com.university.retake.controller;

import com.university.retake.dto.ApiResponse;
import com.university.retake.dto.RetakeCourseDto;
import com.university.retake.service.RetakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller cho Nhóm chức năng 1: Hiển thị danh sách môn cần học lại
 */
@RestController
@RequestMapping("/v1/retake")
@RequiredArgsConstructor
public class RetakeController {

    private final RetakeService retakeService;

    /**
     * API 1: Lấy danh sách môn cần học lại của sinh viên
     * GET /api/v1/retake/students/{studentId}/courses
     */
    @GetMapping("/students/{studentId}/courses")
    public ResponseEntity<ApiResponse<List<RetakeCourseDto>>> getRetakeCourses(
            @PathVariable UUID studentId) {
        List<RetakeCourseDto> result = retakeService.getRetakeCoursesByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.ok(
                "Lấy danh sách môn học lại thành công. Tìm thấy " + result.size() + " môn.",
                result
        ));
    }
}
