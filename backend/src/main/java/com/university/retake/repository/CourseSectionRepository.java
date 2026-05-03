package com.university.retake.repository;

import com.university.retake.entity.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, UUID> {

    /**
     * Lấy danh sách các lớp học phần (course_sections) của một môn học cụ thể
     * trong các học kỳ đang mở (trạng thái = open hoặc planned)
     */
    @Query("SELECT cs FROM CourseSection cs " +
           "WHERE cs.course.id = :courseId " +
           "AND cs.status IN ('open', 'planned') " +
           "AND cs.isActive = true")
    List<CourseSection> findOpenSectionsByCourseId(@Param("courseId") UUID courseId);

    /**
     * Lấy tất cả lớp học phần trong một học kỳ
     */
    @Query("SELECT cs FROM CourseSection cs " +
           "WHERE cs.semester.id = :semesterId " +
           "AND cs.isActive = true")
    List<CourseSection> findBySemesterId(@Param("semesterId") UUID semesterId);

    /**
     * Lấy tất cả lớp học phần đang mở (dùng JOIN FETCH để tránh lazy loading)
     */
    @Query("SELECT cs FROM CourseSection cs " +
           "JOIN FETCH cs.course c " +
           "JOIN FETCH cs.semester s " +
           "WHERE cs.status = 'open' " +
           "AND cs.isActive = true " +
           "ORDER BY c.code ASC")
    List<CourseSection> findAllOpen();

    @Query("SELECT cs FROM CourseSection cs " +
           "JOIN FETCH cs.course c " +
           "JOIN FETCH cs.semester s " +
           "WHERE cs.isActive = true " +
           "ORDER BY c.code ASC")
    List<CourseSection> findAllWithCourseAndSemester();
}
