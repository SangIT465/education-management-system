package com.university.retake.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequestDto {
    private String studentCode;
    private String fullName;
    private String email;
    private String className;
    private String password;
}
