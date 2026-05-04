package com.university.retake.repository;

import com.university.retake.entity.StudentCourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface StudentCourseSectionRepository extends JpaRepository<StudentCourseSection, UUID> {

    /**
     * Lấy lịch sử học phần đã hoàn thành của sinh viên (status = 'completed')
     * dùng để tính điểm tổng kết và xác định môn pass/fail
     */
    @Query("SELECT scs FROM StudentCourseSection scs " +
           "WHERE scs.student.id = :studentId " +
           "AND scs.status = 'completed' " +
           "AND scs.isActive = true")
    List<StudentCourseSection> findCompletedByStudentId(@Param("studentId") UUID studentId);

    /**
     * Lấy lịch sử tất cả các môn của sinh viên
     */
    @Query("SELECT scs FROM StudentCourseSection scs " +
           "WHERE scs.student.id = :studentId " +
           "AND scs.isActive = true")
    List<StudentCourseSection> findByStudentId(@Param("studentId") UUID studentId);

    @Query("SELECT scs FROM StudentCourseSection scs " +
           "WHERE scs.student.id = :studentId " +
           "AND scs.courseSection.id = :sectionId " +
           "AND scs.isActive = true")
    java.util.Optional<StudentCourseSection> findByStudentIdAndSectionId(
            @Param("studentId") UUID studentId,
            @Param("sectionId") UUID sectionId);
}
