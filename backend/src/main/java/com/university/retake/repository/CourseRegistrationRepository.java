package com.university.retake.repository;

import com.university.retake.entity.CourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, UUID> {

    /**
     * Lấy tất cả đăng ký của sinh viên trong đợt đăng ký
     */
    @Query("SELECT cr FROM CourseRegistration cr " +
           "WHERE cr.student.id = :studentId " +
           "AND cr.registrationPeriod.id = :periodId " +
           "AND cr.isActive = true " +
           "AND cr.status != 'CANCELED'")
    List<CourseRegistration> findActiveByStudentAndPeriod(
            @Param("studentId") UUID studentId,
            @Param("periodId") UUID periodId);

    /**
     * Kiểm tra xem sinh viên đã đăng ký lớp này hay chưa
     */
    @Query("SELECT cr FROM CourseRegistration cr " +
           "WHERE cr.student.id = :studentId " +
           "AND cr.courseSection.id = :sectionId " +
           "AND cr.isActive = true " +
           "AND cr.status != 'CANCELED'")
    Optional<CourseRegistration> findExisting(
            @Param("studentId") UUID studentId,
            @Param("sectionId") UUID sectionId);

    /**
     * Lấy danh sách đăng ký của sinh viên
     */
    @Query("SELECT cr FROM CourseRegistration cr " +
           "WHERE cr.student.id = :studentId " +
           "AND cr.isActive = true")
    List<CourseRegistration> findByStudentId(@Param("studentId") UUID studentId);
}
