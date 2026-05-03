package com.university.retake.repository;

import com.university.retake.entity.GradeComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface GradeComponentRepository extends JpaRepository<GradeComponent, UUID> {

    /**
     * Lấy điểm thành phần của một đăng ký học phần (student_course_sections)
     * để tính tổng điểm
     */
    @Query("SELECT gc FROM GradeComponent gc " +
           "WHERE gc.studentCourseSection.id = :scsId " +
           "AND gc.isActive = true")
    List<GradeComponent> findByStudentCourseSectionId(@Param("scsId") UUID scsId);

    @Query("SELECT gc FROM GradeComponent gc " +
           "JOIN FETCH gc.studentCourseSection scs " +
           "JOIN FETCH scs.student st " +
           "JOIN FETCH scs.courseSection cs " +
           "JOIN FETCH cs.course c " +
           "WHERE scs.student.id = :studentId " +
           "AND gc.isActive = true " +
           "ORDER BY c.code ASC, gc.componentCode ASC")
    List<GradeComponent> findByStudentId(@Param("studentId") UUID studentId);
}
