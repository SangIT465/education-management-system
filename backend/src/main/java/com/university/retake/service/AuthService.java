package com.university.retake.service;

import com.university.retake.dto.AuthResponseDto;
import com.university.retake.dto.RegisterRequestDto;
import com.university.retake.entity.Account;
import com.university.retake.entity.Student;
import com.university.retake.repository.AccountRepository;
import com.university.retake.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public AuthResponseDto login(String username, String password) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!Boolean.TRUE.equals(account.getIsActive())) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        if (!account.getPassword().equals(password)) {
            throw new RuntimeException("Mật khẩu không đúng");
        }

        return toDto(account);
    }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto req) {
        if (req.getStudentCode() == null || req.getStudentCode().isBlank()) {
            throw new RuntimeException("Mã sinh viên không được để trống");
        }
        if (req.getFullName() == null || req.getFullName().isBlank()) {
            throw new RuntimeException("Họ tên không được để trống");
        }
        if (req.getPassword() == null || req.getPassword().length() < 4) {
            throw new RuntimeException("Mật khẩu phải có ít nhất 4 ký tự");
        }

        String code = req.getStudentCode().trim().toUpperCase();

        if (accountRepository.existsByUsername(code)) {
            throw new RuntimeException("Mã sinh viên " + code + " đã có tài khoản. Vui lòng đăng nhập.");
        }

        // Tìm hoặc tạo mới bản ghi sinh viên
        Student student = studentRepository.findByStudentCode(code).orElse(null);

        if (student == null) {
            student = Student.builder()
                    .studentCode(code)
                    .fullName(req.getFullName().trim())
                    .email(req.getEmail())
                    .className(req.getClassName())
                    .isActive(true)
                    .build();
            studentRepository.save(student);
        }

        // Tạo tài khoản
        Account account = Account.builder()
                .username(code)
                .password(req.getPassword())
                .role("student")
                .student(student)
                .fullName(req.getFullName().trim())
                .isActive(true)
                .build();

        accountRepository.save(account);
        return toDto(account);
    }

    private AuthResponseDto toDto(Account account) {
        Student s = account.getStudent();
        return AuthResponseDto.builder()
                .id(account.getId().toString())
                .username(account.getUsername())
                .role(account.getRole())
                .fullName(account.getFullName())
                .email(s != null ? s.getEmail() : null)
                .className(s != null ? s.getClassName() : null)
                .studentId(s != null ? s.getId().toString() : null)
                .studentCode(s != null ? s.getStudentCode() : null)
                .build();
    }
}
