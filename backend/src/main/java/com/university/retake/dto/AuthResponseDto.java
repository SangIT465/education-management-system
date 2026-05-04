package com.university.retake.dto;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthResponseDto {
    private String id;          // account UUID
    private String username;    // mã sinh viên hoặc username admin
    private String role;        // "student" | "admin"
    private String fullName;
    private String email;
    private String className;
    private String studentId;   // UUID bảng students (null nếu là admin)
    private String studentCode; // mã sinh viên (null nếu là admin)
}
