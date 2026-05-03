package com.university.retake.service;

import com.university.retake.dto.CourseRegistrationDto;
import com.university.retake.dto.RegistrationRequestDto;
import com.university.retake.entity.*;
import com.university.retake.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service xử lý đăng ký học phần (cho cả học mới và học lại)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegistrationService {

    private final CourseRegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final CourseSectionRepository sectionRepository;
    private final RegistrationPeriodRepository periodRepository;

    /**
     * Đăng ký học phần (học lại hoặc học mới)
     * Logic kiểm tra:
     * - Sinh viên có tồn tại không
     * - Lớp học phần có tồn tại và đang mở không
     * - Đợt đăng ký có đang mở không
     * - Sinh viên đã đăng ký lớp này chưa
     * - Lớp còn slot không
     */
    public CourseRegistrationDto register(RegistrationRequestDto request) {
        log.info("==> Đăng ký học phần: studentId={}, sectionId={}, type={}",
                request.getStudentId(), request.getCourseSectionId(), request.getRegistrationType());

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        CourseSection section = sectionRepository.findById(request.getCourseSectionId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học phần"));

        RegistrationPeriod period = periodRepository.findById(request.getRegistrationPeriodId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt đăng ký"));

        // Kiểm tra đợt đăng ký
        LocalDateTime now = LocalDateTime.now();
        if (Boolean.FALSE.equals(period.getIsOpen())) {
            throw new RuntimeException("Đợt đăng ký đã đóng");
        }
        if (now.isBefore(period.getStartTime())) {
            throw new RuntimeException("Đợt đăng ký chưa mở");
        }
        if (now.isAfter(period.getEndTime())) {
            throw new RuntimeException("Đợt đăng ký đã kết thúc");
        }

        // Kiểm tra lớp học phần
        if (!"open".equalsIgnoreCase(section.getStatus())) {
            throw new RuntimeException("Lớp học phần không mở đăng ký");
        }
        if (Boolean.FALSE.equals(section.getIsActive())) {
            throw new RuntimeException("Lớp học phần đã ngưng hoạt động");
        }

        // Kiểm tra slot
        Integer max = section.getMaxStudents() == null ? 0 : section.getMaxStudents();
        Integer cur = section.getCurrentStudents() == null ? 0 : section.getCurrentStudents();
        if (cur >= max) {
            throw new RuntimeException("Lớp học phần đã đầy");
        }

        // Kiểm tra trùng đăng ký
        registrationRepository.findExisting(student.getId(), section.getId())
                .ifPresent(r -> {
                    throw new RuntimeException("Sinh viên đã đăng ký lớp này rồi");
                });

        // Tạo đăng ký
        CourseRegistration registration = CourseRegistration.builder()
                .student(student)
                .courseSection(section)
                .registrationPeriod(period)
                .registrationType(request.getRegistrationType() == null ? "NEW" : request.getRegistrationType())
                .status("APPROVED") // Tự động duyệt cho demo, thực tế có thể PENDING
                .registeredAt(LocalDateTime.now())
                .note(request.getNote())
                .isActive(true)
                .build();

        CourseRegistration saved = registrationRepository.save(registration);

        // Cập nhật số sinh viên hiện tại trong lớp
        section.setCurrentStudents(cur + 1);
        sectionRepository.save(section);

        log.info("==> Đăng ký thành công: {}", saved.getId());
        return toDto(saved);
    }

    /**
     * Hủy đăng ký
     */
    public CourseRegistrationDto cancel(UUID registrationId) {
        CourseRegistration reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký"));

        if ("CANCELED".equals(reg.getStatus())) {
            throw new RuntimeException("Đăng ký đã bị hủy trước đó");
        }

        reg.setStatus("CANCELED");
        reg.setIsActive(false);
        registrationRepository.save(reg);

        // Giảm số lượng SV trong lớp
        CourseSection section = reg.getCourseSection();
        Integer cur = section.getCurrentStudents() == null ? 0 : section.getCurrentStudents();
        if (cur > 0) {
            section.setCurrentStudents(cur - 1);
            sectionRepository.save(section);
        }

        log.info("==> Hủy đăng ký: {}", registrationId);
        return toDto(reg);
    }

    /**
     * Lấy danh sách đăng ký của sinh viên
     */
    @Transactional(readOnly = true)
    public List<CourseRegistrationDto> getStudentRegistrations(UUID studentId) {
        return registrationRepository.findByStudentId(studentId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CourseRegistrationDto toDto(CourseRegistration r) {
        return CourseRegistrationDto.builder()
                .id(r.getId())
                .studentId(r.getStudent().getId())
                .studentCode(r.getStudent().getStudentCode())
                .studentName(r.getStudent().getFullName())
                .courseSectionId(r.getCourseSection().getId())
                .sectionCode(r.getCourseSection().getCode())
                .courseName(r.getCourseSection().getCourse() != null ? r.getCourseSection().getCourse().getName() : null)
                .credits(r.getCourseSection().getCourse() != null ? r.getCourseSection().getCourse().getCredits() : 0)
                .registrationType(r.getRegistrationType())
                .status(r.getStatus())
                .registeredAt(r.getRegisteredAt())
                .note(r.getNote())
                .build();
    }
}
