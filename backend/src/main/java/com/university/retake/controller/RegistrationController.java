package com.university.retake.controller;

import com.university.retake.dto.ApiResponse;
import com.university.retake.dto.CourseRegistrationDto;
import com.university.retake.dto.RegistrationRequestDto;
import com.university.retake.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller cho Nhóm chức năng 2: Đăng ký học phần (học lại / học mới)
 */
@RestController
@RequestMapping("/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * API 2: Đăng ký học phần (học lại hoặc học mới)
     * POST /api/v1/registrations
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CourseRegistrationDto>> register(
            @Valid @RequestBody RegistrationRequestDto request) {
        CourseRegistrationDto result = registrationService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("Đăng ký học phần thành công", result));
    }

    /**
     * API 3: Hủy đăng ký
     * DELETE /api/v1/registrations/{registrationId}
     */
    @DeleteMapping("/{registrationId}")
    public ResponseEntity<ApiResponse<CourseRegistrationDto>> cancel(
            @PathVariable UUID registrationId) {
        CourseRegistrationDto result = registrationService.cancel(registrationId);
        return ResponseEntity.ok(ApiResponse.ok("Hủy đăng ký thành công", result));
    }

    /**
     * API 4: Lấy danh sách đăng ký của sinh viên
     * GET /api/v1/registrations/students/{studentId}
     */
    @GetMapping("/students/{studentId}")
    public ResponseEntity<ApiResponse<List<CourseRegistrationDto>>> getStudentRegistrations(
            @PathVariable UUID studentId) {
        List<CourseRegistrationDto> result = registrationService.getStudentRegistrations(studentId);
        return ResponseEntity.ok(ApiResponse.ok(
                "Tìm thấy " + result.size() + " đăng ký",
                result
        ));
    }
}
