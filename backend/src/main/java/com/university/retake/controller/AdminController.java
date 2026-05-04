package com.university.retake.controller;

import com.university.retake.dto.ApiResponse;
import com.university.retake.entity.*;
import com.university.retake.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final CourseSectionRepository sectionRepository;
    private final GradeComponentRepository gradeComponentRepository;
    private final RegistrationPeriodRepository periodRepository;
    private final StudentCourseSectionRepository scsRepository;

    // ========== STUDENTS ==========

    @GetMapping("/students")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStudents() {
        List<Map<String, Object>> result = studentRepository.findAll().stream()
                .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                .map(s -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", s.getId());
                    m.put("studentCode", s.getStudentCode());
                    m.put("fullName", s.getFullName());
                    m.put("email", s.getEmail());
                    m.put("className", s.getClassName());
                    return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping("/students")
    @Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> createStudent(@RequestBody Map<String, Object> body) {
        Student s = new Student();
        s.setStudentCode((String) body.get("studentCode"));
        s.setFullName((String) body.get("fullName"));
        s.setEmail((String) body.get("email"));
        s.setClassName((String) body.get("className"));
        s.setIsActive(true);
        studentRepository.save(s);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", s.getId());
        m.put("studentCode", s.getStudentCode());
        m.put("fullName", s.getFullName());
        return ResponseEntity.ok(ApiResponse.ok(m));
    }

    @PutMapping("/students/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> updateStudent(@PathVariable UUID id,
                                                             @RequestBody Map<String, Object> body) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
        if (body.containsKey("studentCode")) s.setStudentCode((String) body.get("studentCode"));
        if (body.containsKey("fullName"))    s.setFullName((String) body.get("fullName"));
        if (body.containsKey("email"))       s.setEmail((String) body.get("email"));
        if (body.containsKey("className"))   s.setClassName((String) body.get("className"));
        studentRepository.save(s);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật sinh viên thành công"));
    }

    @DeleteMapping("/students/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable UUID id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
        s.setIsActive(false);
        studentRepository.save(s);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa sinh viên"));
    }

    // ========== COURSES ==========

    @GetMapping("/courses")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCourses() {
        List<Map<String, Object>> result = courseRepository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                .map(c -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", c.getId());
                    m.put("code", c.getCode());
                    m.put("name", c.getName());
                    m.put("credits", c.getCredits());
                    m.put("description", c.getDescription());
                    return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping("/courses")
    @Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> createCourse(@RequestBody Map<String, Object> body) {
        Course c = new Course();
        c.setCode((String) body.get("code"));
        c.setName((String) body.get("name"));
        c.setCredits(toInt(body.get("credits")));
        c.setDescription((String) body.get("description"));
        c.setIsActive(true);
        courseRepository.save(c);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", c.getId());
        m.put("code", c.getCode());
        m.put("name", c.getName());
        return ResponseEntity.ok(ApiResponse.ok(m));
    }

    @PutMapping("/courses/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> updateCourse(@PathVariable UUID id,
                                                            @RequestBody Map<String, Object> body) {
        Course c = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));
        if (body.containsKey("code"))        c.setCode((String) body.get("code"));
        if (body.containsKey("name"))        c.setName((String) body.get("name"));
        if (body.containsKey("credits"))     c.setCredits(toInt(body.get("credits")));
        if (body.containsKey("description")) c.setDescription((String) body.get("description"));
        courseRepository.save(c);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật môn học thành công"));
    }

    @DeleteMapping("/courses/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteCourse(@PathVariable UUID id) {
        Course c = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));
        c.setIsActive(false);
        courseRepository.save(c);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa môn học"));
    }

    // ========== SEMESTERS ==========

    @GetMapping("/semesters")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSemesters() {
        List<Map<String, Object>> result = semesterRepository.findAll().stream()
                .filter(sem -> Boolean.TRUE.equals(sem.getIsActive()))
                .map(sem -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", sem.getId());
                    m.put("code", sem.getCode());
                    m.put("name", sem.getName());
                    m.put("academicYear", sem.getAcademicYear());
                    m.put("startDate", sem.getStartDate());
                    m.put("endDate", sem.getEndDate());
                    return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping("/semesters")
    @Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> createSemester(@RequestBody Map<String, Object> body) {
        Semester sem = new Semester();
        sem.setCode((String) body.get("code"));
        sem.setName((String) body.get("name"));
        sem.setAcademicYear((String) body.get("academicYear"));
        if (body.get("startDate") != null) sem.setStartDate(LocalDate.parse((String) body.get("startDate")));
        if (body.get("endDate") != null)   sem.setEndDate(LocalDate.parse((String) body.get("endDate")));
        sem.setIsActive(true);
        semesterRepository.save(sem);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", sem.getId());
        m.put("code", sem.getCode());
        m.put("name", sem.getName());
        return ResponseEntity.ok(ApiResponse.ok(m));
    }

    @PutMapping("/semesters/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> updateSemester(@PathVariable UUID id,
                                                              @RequestBody Map<String, Object> body) {
        Semester sem = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học kỳ"));
        if (body.containsKey("code"))         sem.setCode((String) body.get("code"));
        if (body.containsKey("name"))         sem.setName((String) body.get("name"));
        if (body.containsKey("academicYear")) sem.setAcademicYear((String) body.get("academicYear"));
        if (body.get("startDate") != null)    sem.setStartDate(LocalDate.parse((String) body.get("startDate")));
        if (body.get("endDate") != null)      sem.setEndDate(LocalDate.parse((String) body.get("endDate")));
        semesterRepository.save(sem);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật học kỳ thành công"));
    }

    @DeleteMapping("/semesters/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteSemester(@PathVariable UUID id) {
        Semester sem = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học kỳ"));
        sem.setIsActive(false);
        semesterRepository.save(sem);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa học kỳ"));
    }

    // ========== COURSE SECTIONS ==========

    @GetMapping("/course-sections")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCourseSections() {
        List<Map<String, Object>> result = sectionRepository.findAllWithCourseAndSemester().stream()
                .map(cs -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", cs.getId());
                    m.put("code", cs.getCode());
                    m.put("name", cs.getName());
                    m.put("courseId", cs.getCourse().getId());
                    m.put("courseCode", cs.getCourse().getCode());
                    m.put("courseName", cs.getCourse().getName());
                    m.put("credits", cs.getCourse().getCredits());
                    m.put("semesterId", cs.getSemester().getId());
                    m.put("semesterName", cs.getSemester().getName());
                    m.put("classType", cs.getClassType());
                    m.put("maxStudents", cs.getMaxStudents());
                    m.put("currentStudents", cs.getCurrentStudents());
                    m.put("status", cs.getStatus());
                    m.put("registrationStart", cs.getRegistrationStart());
                    m.put("registrationEnd", cs.getRegistrationEnd());
                    return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping("/course-sections")
    @Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> createCourseSection(@RequestBody Map<String, Object> body) {
        CourseSection cs = new CourseSection();
        cs.setCode((String) body.get("code"));
        cs.setName((String) body.get("name"));
        UUID courseId = UUID.fromString((String) body.get("courseId"));
        cs.setCourse(courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học")));
        UUID semId = UUID.fromString((String) body.get("semesterId"));
        cs.setSemester(semesterRepository.findById(semId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học kỳ")));
        cs.setClassType((String) body.get("classType"));
        cs.setMaxStudents(toInt(body.getOrDefault("maxStudents", 50)));
        cs.setCurrentStudents(0);
        cs.setStatus((String) body.getOrDefault("status", "open"));
        if (body.get("registrationStart") != null)
            cs.setRegistrationStart(LocalDateTime.parse(padDateTime((String) body.get("registrationStart"))));
        if (body.get("registrationEnd") != null)
            cs.setRegistrationEnd(LocalDateTime.parse(padDateTime((String) body.get("registrationEnd"))));
        cs.setIsActive(true);
        sectionRepository.save(cs);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", cs.getId());
        m.put("code", cs.getCode());
        return ResponseEntity.ok(ApiResponse.ok(m));
    }

    @PutMapping("/course-sections/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> updateCourseSection(@PathVariable UUID id,
                                                                    @RequestBody Map<String, Object> body) {
        CourseSection cs = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học phần"));
        if (body.containsKey("code"))        cs.setCode((String) body.get("code"));
        if (body.containsKey("name"))        cs.setName((String) body.get("name"));
        if (body.containsKey("classType"))   cs.setClassType((String) body.get("classType"));
        if (body.containsKey("maxStudents")) cs.setMaxStudents(toInt(body.get("maxStudents")));
        if (body.containsKey("status"))      cs.setStatus((String) body.get("status"));
        if (body.containsKey("courseId")) {
            UUID cId = UUID.fromString((String) body.get("courseId"));
            cs.setCourse(courseRepository.findById(cId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học")));
        }
        if (body.containsKey("semesterId")) {
            UUID sId = UUID.fromString((String) body.get("semesterId"));
            cs.setSemester(semesterRepository.findById(sId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy học kỳ")));
        }
        if (body.get("registrationStart") != null)
            cs.setRegistrationStart(LocalDateTime.parse(padDateTime((String) body.get("registrationStart"))));
        if (body.get("registrationEnd") != null)
            cs.setRegistrationEnd(LocalDateTime.parse(padDateTime((String) body.get("registrationEnd"))));
        sectionRepository.save(cs);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật lớp học phần thành công"));
    }

    @DeleteMapping("/course-sections/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteCourseSection(@PathVariable UUID id) {
        CourseSection cs = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học phần"));
        cs.setIsActive(false);
        sectionRepository.save(cs);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa lớp học phần"));
    }

    // ========== GRADE COMPONENTS ==========

    @GetMapping("/grade-components")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getGradeComponents(
            @RequestParam(required = false) UUID studentId) {
        if (studentId == null) {
            return ResponseEntity.ok(ApiResponse.ok(Collections.emptyList()));
        }
        List<GradeComponent> components = gradeComponentRepository.findByStudentId(studentId);
        List<Map<String, Object>> result = components.stream().map(gc -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", gc.getId());
            m.put("componentCode", gc.getComponentCode());
            m.put("componentName", gc.getComponentName());
            m.put("weightPercentage", gc.getWeightPercentage());
            m.put("score", gc.getScore());
            StudentCourseSection scs = gc.getStudentCourseSection();
            m.put("scsId", scs.getId());
            m.put("studentId", scs.getStudent().getId());
            m.put("studentName", scs.getStudent().getFullName());
            m.put("studentCode", scs.getStudent().getStudentCode());
            m.put("sectionCode", scs.getCourseSection().getCode());
            m.put("courseName", scs.getCourseSection().getCourse().getName());
            m.put("courseCode", scs.getCourseSection().getCourse().getCode());
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping("/grade-components")
    @Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> createGradeComponents(@RequestBody Map<String, Object> body) {
        UUID studentId  = UUID.fromString((String) body.get("studentId"));
        UUID sectionId  = UUID.fromString((String) body.get("courseSectionId"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
        CourseSection section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học phần"));

        // Tìm hoặc tạo student_course_section với status=completed
        StudentCourseSection scs = scsRepository
                .findByStudentIdAndSectionId(studentId, sectionId)
                .orElse(null);

        if (scs == null) {
            scs = new StudentCourseSection();
            scs.setStudent(student);
            scs.setCourseSection(section);
            scs.setStatus("completed");
            scs.setRegisteredAt(LocalDateTime.now());
            scs.setIsActive(true);
            scsRepository.save(scs);
        } else {
            scs.setStatus("completed");
            scsRepository.save(scs);
        }

        // Xóa điểm cũ nếu có (ghi đè) — dùng deleteAllInBatch để tránh N+1 queries
        List<GradeComponent> old = gradeComponentRepository.findByStudentCourseSectionId(scs.getId());
        if (!old.isEmpty()) {
            gradeComponentRepository.deleteAllInBatch(old);
        }

        // Tạo điểm thành phần mới
        List<?> components = (List<?>) body.get("components");
        BigDecimal totalScore = BigDecimal.ZERO;

        for (Object comp : components) {
            Map<?, ?> c = (Map<?, ?>) comp;
            GradeComponent gc = new GradeComponent();
            gc.setStudentCourseSection(scs);
            gc.setComponentCode((String) c.get("componentCode"));
            gc.setComponentName((String) c.get("componentName"));
            gc.setWeightPercentage(new BigDecimal(c.get("weightPercentage").toString()));
            gc.setScore(new BigDecimal(c.get("score").toString()));
            gc.setIsActive(true);
            gradeComponentRepository.save(gc);
            totalScore = totalScore.add(
                gc.getScore().multiply(gc.getWeightPercentage())
                             .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP)
            );
        }

        // Phân loại tự động
        String status = totalScore.compareTo(new BigDecimal("5.0")) < 0 ? "FAILED"
                      : totalScore.compareTo(new BigDecimal("7.0")) < 0 ? "IMPROVABLE"
                      : "PASSED";
        String label  = "FAILED".equals(status) ? "Cần học lại"
                      : "IMPROVABLE".equals(status) ? "Có thể cải thiện" : "Đạt";

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("scsId", scs.getId());
        result.put("totalScore", totalScore);
        result.put("status", status);
        result.put("label", label);
        result.put("componentCount", components.size());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @DeleteMapping("/grade-components/section/{scsId}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteGradesByScs(@PathVariable UUID scsId) {
        List<GradeComponent> list = gradeComponentRepository.findByStudentCourseSectionId(scsId);
        gradeComponentRepository.deleteAll(list);
        StudentCourseSection scs = scsRepository.findById(scsId).orElse(null);
        if (scs != null) { scs.setIsActive(false); scsRepository.save(scs); }
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa điểm"));
    }

    @PutMapping("/grade-components/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> updateGradeComponent(@PathVariable UUID id,
                                                                     @RequestBody Map<String, Object> body) {
        GradeComponent gc = gradeComponentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy điểm thành phần"));
        if (body.containsKey("score") && body.get("score") != null)
            gc.setScore(new BigDecimal(body.get("score").toString()));
        if (body.containsKey("componentName") && body.get("componentName") != null)
            gc.setComponentName((String) body.get("componentName"));
        if (body.containsKey("weightPercentage") && body.get("weightPercentage") != null)
            gc.setWeightPercentage(new BigDecimal(body.get("weightPercentage").toString()));
        gradeComponentRepository.save(gc);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật điểm thành công"));
    }

    // ========== REGISTRATION PERIODS ==========

    @GetMapping("/registration-periods")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRegistrationPeriods() {
        List<Map<String, Object>> result = periodRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
                .map(p -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", p.getId());
                    m.put("name", p.getName());
                    m.put("semesterId", p.getSemester().getId());
                    m.put("semesterName", p.getSemester().getName());
                    m.put("startTime", p.getStartTime());
                    m.put("endTime", p.getEndTime());
                    m.put("maxCredits", p.getMaxCredits());
                    m.put("minCredits", p.getMinCredits());
                    m.put("allowRetake", p.getAllowRetake());
                    m.put("isOpen", p.getIsOpen());
                    return m;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping("/registration-periods")
    @Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> createRegistrationPeriod(@RequestBody Map<String, Object> body) {
        RegistrationPeriod p = new RegistrationPeriod();
        p.setName((String) body.get("name"));
        UUID semId = UUID.fromString((String) body.get("semesterId"));
        p.setSemester(semesterRepository.findById(semId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học kỳ")));
        p.setStartTime(LocalDateTime.parse(padDateTime((String) body.get("startTime"))));
        p.setEndTime(LocalDateTime.parse(padDateTime((String) body.get("endTime"))));
        p.setMaxCredits(toInt(body.getOrDefault("maxCredits", 25)));
        p.setMinCredits(toInt(body.getOrDefault("minCredits", 12)));
        p.setAllowRetake(toBool(body.getOrDefault("allowRetake", true)));
        p.setIsOpen(toBool(body.getOrDefault("isOpen", true)));
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        p.setIsActive(true);
        periodRepository.save(p);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getId());
        m.put("name", p.getName());
        return ResponseEntity.ok(ApiResponse.ok(m));
    }

    @PutMapping("/registration-periods/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> updateRegistrationPeriod(@PathVariable UUID id,
                                                                         @RequestBody Map<String, Object> body) {
        RegistrationPeriod p = periodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt đăng ký"));
        if (body.containsKey("name"))        p.setName((String) body.get("name"));
        if (body.containsKey("maxCredits"))  p.setMaxCredits(toInt(body.get("maxCredits")));
        if (body.containsKey("minCredits"))  p.setMinCredits(toInt(body.get("minCredits")));
        if (body.containsKey("allowRetake")) p.setAllowRetake(toBool(body.get("allowRetake")));
        if (body.containsKey("isOpen"))      p.setIsOpen(toBool(body.get("isOpen")));
        if (body.containsKey("startTime") && body.get("startTime") != null)
            p.setStartTime(LocalDateTime.parse(padDateTime((String) body.get("startTime"))));
        if (body.containsKey("endTime") && body.get("endTime") != null)
            p.setEndTime(LocalDateTime.parse(padDateTime((String) body.get("endTime"))));
        if (body.containsKey("semesterId")) {
            UUID sId = UUID.fromString((String) body.get("semesterId"));
            p.setSemester(semesterRepository.findById(sId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy học kỳ")));
        }
        p.setUpdatedAt(LocalDateTime.now());
        periodRepository.save(p);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật đợt đăng ký thành công"));
    }

    @DeleteMapping("/registration-periods/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteRegistrationPeriod(@PathVariable UUID id) {
        RegistrationPeriod p = periodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt đăng ký"));
        p.setIsActive(false);
        p.setIsOpen(false);
        periodRepository.save(p);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa đợt đăng ký"));
    }

    // ========== HELPERS ==========

    private int toInt(Object val) {
        if (val == null) return 0;
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(val.toString()); } catch (Exception e) { return 0; }
    }

    private boolean toBool(Object val) {
        if (val == null) return false;
        if (val instanceof Boolean) return (Boolean) val;
        return Boolean.parseBoolean(val.toString());
    }

    private String padDateTime(String dt) {
        if (dt == null) return null;
        if (dt.length() == 16) return dt + ":00";
        return dt;
    }
}
