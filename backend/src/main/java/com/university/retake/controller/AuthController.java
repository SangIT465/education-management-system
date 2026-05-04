package com.university.retake.controller;

import com.university.retake.dto.ApiResponse;
import com.university.retake.dto.AuthResponseDto;
import com.university.retake.dto.LoginRequestDto;
import com.university.retake.dto.RegisterRequestDto;
import com.university.retake.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** POST /api/v1/auth/login — dùng cho cả sinh viên và admin */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody LoginRequestDto req) {
        try {
            AuthResponseDto data = authService.login(req.getUsername(), req.getPassword());
            return ResponseEntity.ok(ApiResponse.ok("Đăng nhập thành công", data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** POST /api/v1/auth/register — chỉ dành cho sinh viên tự đăng ký */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@RequestBody RegisterRequestDto req) {
        try {
            AuthResponseDto data = authService.register(req);
            return ResponseEntity.ok(ApiResponse.ok("Đăng ký thành công", data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
