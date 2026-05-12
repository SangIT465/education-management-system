package com.university.retake.config;

import com.university.retake.entity.*;
import com.university.retake.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final SemesterRepository semesterRepo;
    private final CourseRepository courseRepo;
    private final StudentRepository studentRepo;
    private final CourseSectionRepository sectionRepo;
    private final StudentCourseSectionRepository scsRepo;
    private final GradeComponentRepository gradeRepo;
    private final RegistrationPeriodRepository periodRepo;
    private final AccountRepository accountRepo;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (semesterRepo.count() > 0) return;

        // SEMESTERS
        Semester sem1 = semesterRepo.save(Semester.builder()
                .code("HK1-2024").name("Học kỳ 1 - 2024-2025").academicYear("2024-2025")
                .startDate(LocalDate.of(2024,9,1)).endDate(LocalDate.of(2025,1,15)).isActive(true).build());
        Semester sem2 = semesterRepo.save(Semester.builder()
                .code("HK2-2024").name("Học kỳ 2 - 2024-2025").academicYear("2024-2025")
                .startDate(LocalDate.of(2025,2,1)).endDate(LocalDate.of(2025,6,30)).isActive(true).build());
        Semester sem3 = semesterRepo.save(Semester.builder()
                .code("HK1-2025").name("Học kỳ 1 - 2025-2026").academicYear("2025-2026")
                .startDate(LocalDate.of(2025,9,1)).endDate(LocalDate.of(2026,1,15)).isActive(true).build());

        // COURSES
        Course c1 = courseRepo.save(Course.builder().code("IT101").name("Nhập môn Lập trình").credits(3).description("Cơ bản về lập trình C").isActive(true).build());
        Course c2 = courseRepo.save(Course.builder().code("IT102").name("Cấu trúc dữ liệu và Giải thuật").credits(4).description("DSA cơ bản").isActive(true).build());
        Course c3 = courseRepo.save(Course.builder().code("IT201").name("Cơ sở dữ liệu").credits(3).description("SQL và thiết kế CSDL").isActive(true).build());
        Course c4 = courseRepo.save(Course.builder().code("IT202").name("Lập trình hướng đối tượng").credits(3).description("OOP với Java").isActive(true).build());
        Course c5 = courseRepo.save(Course.builder().code("IT301").name("Phát triển Web").credits(4).description("HTML/CSS/JS, Spring Boot").isActive(true).build());
        Course c6 = courseRepo.save(Course.builder().code("MA101").name("Toán cao cấp 1").credits(3).description("Giải tích 1 biến").isActive(true).build());

        // STUDENTS
        Student s1 = studentRepo.save(Student.builder().studentCode("SV2024001").fullName("Nguyễn Văn An").email("an.nv@uni.edu.vn").className("CNTT-K17A").isActive(true).build());
        Student s2 = studentRepo.save(Student.builder().studentCode("SV2024002").fullName("Trần Thị Bình").email("binh.tt@uni.edu.vn").className("CNTT-K17A").isActive(true).build());
        Student s3 = studentRepo.save(Student.builder().studentCode("SV2024003").fullName("Lê Hoàng Cường").email("cuong.lh@uni.edu.vn").className("CNTT-K17B").isActive(true).build());

        // COURSE SECTIONS
        CourseSection cs1 = sectionRepo.save(CourseSection.builder().code("IT101-01").name("IT101 - Lớp 01").course(c1).semester(sem1).maxStudents(50).currentStudents(45).classType("theory").status("closed").registrationStart(LocalDateTime.of(2024,8,1,0,0)).registrationEnd(LocalDateTime.of(2024,8,25,0,0)).isActive(true).build());
        CourseSection cs2 = sectionRepo.save(CourseSection.builder().code("IT102-01").name("IT102 - Lớp 01").course(c2).semester(sem1).maxStudents(50).currentStudents(48).classType("theory").status("closed").registrationStart(LocalDateTime.of(2024,8,1,0,0)).registrationEnd(LocalDateTime.of(2024,8,25,0,0)).isActive(true).build());
        sectionRepo.save(CourseSection.builder().code("IT201-01").name("IT201 - Lớp 01").course(c3).semester(sem2).maxStudents(50).currentStudents(50).classType("theory").status("closed").registrationStart(LocalDateTime.of(2025,1,15,0,0)).registrationEnd(LocalDateTime.of(2025,1,30,0,0)).isActive(true).build());
        sectionRepo.save(CourseSection.builder().code("IT101-RT01").name("IT101 - Lớp Học Lại").course(c1).semester(sem2).maxStudents(30).currentStudents(5).classType("Lớp học lại").status("open").registrationStart(LocalDateTime.of(2025,1,15,0,0)).registrationEnd(LocalDateTime.of(2025,1,30,0,0)).isActive(true).build());
        sectionRepo.save(CourseSection.builder().code("IT102-RT01").name("IT102 - Lớp Học Lại").course(c2).semester(sem2).maxStudents(30).currentStudents(3).classType("Lớp học lại").status("open").registrationStart(LocalDateTime.of(2025,1,15,0,0)).registrationEnd(LocalDateTime.of(2025,1,30,0,0)).isActive(true).build());
        sectionRepo.save(CourseSection.builder().code("IT202-01").name("IT202 - Lớp 01").course(c4).semester(sem3).maxStudents(50).currentStudents(20).classType("theory").status("open").registrationStart(LocalDateTime.of(2025,8,1,0,0)).registrationEnd(LocalDateTime.of(2026,8,25,0,0)).isActive(true).build());
        sectionRepo.save(CourseSection.builder().code("MA101-01").name("MA101 - Lớp 01").course(c6).semester(sem3).maxStudents(80).currentStudents(60).classType("theory").status("open").registrationStart(LocalDateTime.of(2025,8,1,0,0)).registrationEnd(LocalDateTime.of(2026,8,25,0,0)).isActive(true).build());
        sectionRepo.save(CourseSection.builder().code("IT301-01").name("IT301 - Lớp 01").course(c5).semester(sem3).maxStudents(50).currentStudents(25).classType("hybrid").status("open").registrationStart(LocalDateTime.of(2025,8,1,0,0)).registrationEnd(LocalDateTime.of(2026,8,25,0,0)).isActive(true).build());

        // STUDENT COURSE SECTIONS
        StudentCourseSection scs1 = scsRepo.save(StudentCourseSection.builder().student(s1).courseSection(cs1).status("completed").registeredAt(LocalDateTime.of(2024,8,15,0,0)).note("Đã thi xong").isActive(true).build());
        StudentCourseSection scs2 = scsRepo.save(StudentCourseSection.builder().student(s1).courseSection(cs2).status("completed").registeredAt(LocalDateTime.of(2024,8,15,0,0)).note("Đã thi xong").isActive(true).build());
        StudentCourseSection scs4 = scsRepo.save(StudentCourseSection.builder().student(s2).courseSection(cs1).status("completed").registeredAt(LocalDateTime.of(2024,8,15,0,0)).note("Đã thi xong").isActive(true).build());
        StudentCourseSection scs5 = scsRepo.save(StudentCourseSection.builder().student(s2).courseSection(cs2).status("completed").registeredAt(LocalDateTime.of(2024,8,15,0,0)).note("Đã thi xong").isActive(true).build());

        // GRADE COMPONENTS
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs1).componentCode("CC").componentName("Chuyên cần").weightPercentage(new BigDecimal("10.00")).score(new BigDecimal("8.0")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs1).componentCode("GK").componentName("Giữa kỳ").weightPercentage(new BigDecimal("30.00")).score(new BigDecimal("4.0")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs1).componentCode("CK").componentName("Cuối kỳ").weightPercentage(new BigDecimal("60.00")).score(new BigDecimal("4.0")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs2).componentCode("CC").componentName("Chuyên cần").weightPercentage(new BigDecimal("10.00")).score(new BigDecimal("9.0")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs2).componentCode("GK").componentName("Giữa kỳ").weightPercentage(new BigDecimal("30.00")).score(new BigDecimal("7.0")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs2).componentCode("CK").componentName("Cuối kỳ").weightPercentage(new BigDecimal("60.00")).score(new BigDecimal("7.5")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs4).componentCode("CC").componentName("Chuyên cần").weightPercentage(new BigDecimal("10.00")).score(new BigDecimal("5.0")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs4).componentCode("GK").componentName("Giữa kỳ").weightPercentage(new BigDecimal("30.00")).score(new BigDecimal("3.0")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs4).componentCode("CK").componentName("Cuối kỳ").weightPercentage(new BigDecimal("60.00")).score(new BigDecimal("2.5")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs5).componentCode("CC").componentName("Chuyên cần").weightPercentage(new BigDecimal("10.00")).score(new BigDecimal("7.0")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs5).componentCode("GK").componentName("Giữa kỳ").weightPercentage(new BigDecimal("30.00")).score(new BigDecimal("5.0")).isActive(true).build());
        gradeRepo.save(GradeComponent.builder().studentCourseSection(scs5).componentCode("CK").componentName("Cuối kỳ").weightPercentage(new BigDecimal("60.00")).score(new BigDecimal("4.5")).isActive(true).build());

        // REGISTRATION PERIODS
        periodRepo.save(RegistrationPeriod.builder().name("Đợt 1 - HK1 2025-2026 (Học lại)").semester(sem3).startTime(LocalDateTime.of(2025,8,1,0,0)).endTime(LocalDateTime.of(2026,8,10,0,0)).targetConfig("{\"target\":\"retake_only\"}").maxCredits(25).minCredits(0).allowRetake(true).isOpen(true).isActive(true).build());
        periodRepo.save(RegistrationPeriod.builder().name("Đợt 2 - HK1 2025-2026 (Học phần)").semester(sem3).startTime(LocalDateTime.of(2026,8,11,0,0)).endTime(LocalDateTime.of(2026,8,25,0,0)).targetConfig("{\"target\":\"all\"}").maxCredits(25).minCredits(12).allowRetake(true).isOpen(true).isActive(true).build());

        // ACCOUNTS
        accountRepo.save(Account.builder().username("admin").password("admin123").role("admin").fullName("Quản trị viên").isActive(true).build());
        accountRepo.save(Account.builder().username("SV2024001").password("sv001").role("student").student(s1).fullName(s1.getFullName()).isActive(true).build());
        accountRepo.save(Account.builder().username("SV2024002").password("sv002").role("student").student(s2).fullName(s2.getFullName()).isActive(true).build());
        accountRepo.save(Account.builder().username("SV2024003").password("sv003").role("student").student(s3).fullName(s3.getFullName()).isActive(true).build());

        // 5 CLASS STUDENTS
        seedClassStudents(s3);
    }

    private void seedClassStudents(Student placeholder) {
        String[][] st23a = {
            {"ST23A001","Nguyễn Văn An"},{"ST23A002","Trần Thị Anh"},{"ST23A003","Lê Văn Bình"},
            {"ST23A004","Phạm Thị Bảo"},{"ST23A005","Hoàng Văn Cường"},{"ST23A006","Vũ Thị Chi"},
            {"ST23A007","Đỗ Văn Dũng"},{"ST23A008","Bùi Thị Diễm"},{"ST23A009","Hồ Văn Đạt"},
            {"ST23A010","Đặng Thị Giang"},{"ST23A011","Ngô Văn Hải"},{"ST23A012","Dương Thị Hằng"},
            {"ST23A013","Lý Văn Hùng"},{"ST23A014","Phan Thị Hoa"},{"ST23A015","Hà Văn Khoa"},
            {"ST23A016","Nguyễn Thị Hương"},{"ST23A017","Trần Văn Lâm"},{"ST23A018","Lê Thị Lan"},
            {"ST23A019","Phạm Văn Long"},{"ST23A020","Hoàng Thị Linh"},{"ST23A021","Vũ Văn Minh"},
            {"ST23A022","Đỗ Thị Mai"},{"ST23A023","Bùi Văn Nam"},{"ST23A024","Hồ Thị Ngọc"},
            {"ST23A025","Đặng Văn Nhân"},{"ST23A026","Ngô Thị Như"},{"ST23A027","Dương Văn Phúc"},
            {"ST23A028","Lý Thị Phương"},{"ST23A029","Phan Văn Quân"},{"ST23A030","Hà Thị Thảo"}
        };
        seedClass(st23a, "ST23A");

        String[][] lm23a = {
            {"LM23A001","Nguyễn Văn Sơn"},{"LM23A002","Trần Thị Trang"},{"LM23A003","Lê Văn Thắng"},
            {"LM23A004","Phạm Thị Thư"},{"LM23A005","Hoàng Văn Toàn"},{"LM23A006","Vũ Thị Uyên"},
            {"LM23A007","Đỗ Văn Trung"},{"LM23A008","Bùi Thị Vân"},{"LM23A009","Hồ Văn Tuấn"},
            {"LM23A010","Đặng Thị Yến"},{"LM23A011","Ngô Văn Tài"},{"LM23A012","Dương Thị Liên"},
            {"LM23A013","Lý Văn Việt"},{"LM23A014","Phan Thị Nhi"},{"LM23A015","Hà Văn Dương"},
            {"LM23A016","Nguyễn Thị Hiền"},{"LM23A017","Trần Văn Khải"},{"LM23A018","Lê Thị Loan"},
            {"LM23A019","Phạm Văn Hậu"},{"LM23A020","Hoàng Thị Mỹ"},{"LM23A021","Vũ Văn Điền"},
            {"LM23A022","Đỗ Thị Nga"},{"LM23A023","Bùi Văn Giang"},{"LM23A024","Hồ Thị Kim"},
            {"LM23A025","Đặng Văn Hưng"}
        };
        seedClass(lm23a, "LM23A");

        String[][] el23t = {
            {"EL23T001","Ngô Văn Bảo"},{"EL23T002","Dương Thị Bình"},{"EL23T003","Lý Văn Chiến"},
            {"EL23T004","Phan Thị Cẩm"},{"EL23T005","Hà Văn Công"},{"EL23T006","Nguyễn Thị Diệu"},
            {"EL23T007","Trần Văn Đông"},{"EL23T008","Lê Thị Đoan"},{"EL23T009","Phạm Văn Hào"},
            {"EL23T010","Hoàng Thị Hạnh"},{"EL23T011","Vũ Văn Hiếu"},{"EL23T012","Đỗ Thị Huệ"},
            {"EL23T013","Bùi Văn Khánh"},{"EL23T014","Hồ Thị Khanh"},{"EL23T015","Đặng Văn Kiên"},
            {"EL23T016","Ngô Thị Lê"},{"EL23T017","Dương Văn Lực"},{"EL23T018","Lý Thị Lộc"},
            {"EL23T019","Phan Văn Mạnh"},{"EL23T020","Hà Thị Minh"},{"EL23T021","Nguyễn Văn Nghĩa"},
            {"EL23T022","Trần Thị Nhung"},{"EL23T023","Lê Văn Phong"},{"EL23T024","Phạm Thị Quỳnh"},
            {"EL23T025","Hoàng Văn Quý"},{"EL23T026","Vũ Thị Sương"},{"EL23T027","Đỗ Văn Tâm"},
            {"EL23T028","Bùi Thị Thúy"},{"EL23T029","Hồ Văn Thiện"},{"EL23T030","Đặng Thị Thu"},
            {"EL23T031","Ngô Văn Thuận"},{"EL23T032","Dương Thị Tuyền"},{"EL23T033","Lý Văn Vinh"},
            {"EL23T034","Phan Thị Xuân"},{"EL23T035","Hà Văn Định"}
        };
        seedClass(el23t, "EL23T");

        String[][] ai23d = {
            {"AI23D001","Nguyễn Văn Anh"},{"AI23D002","Trần Thị Châu"},{"AI23D003","Lê Văn Chính"},
            {"AI23D004","Phạm Thị Dung"},{"AI23D005","Hoàng Văn Đức"},{"AI23D006","Vũ Thị Thủy"},
            {"AI23D007","Đỗ Văn Hưng"},{"AI23D008","Bùi Thị Huyền"},{"AI23D009","Hồ Văn Khang"},
            {"AI23D010","Đặng Thị Khánh"},{"AI23D011","Ngô Văn Lợi"},{"AI23D012","Dương Thị Luyến"},
            {"AI23D013","Lý Văn Ninh"},{"AI23D014","Phan Thị Oanh"},{"AI23D015","Hà Văn Phát"},
            {"AI23D016","Nguyễn Thị Quyên"},{"AI23D017","Trần Văn Sang"},{"AI23D018","Lê Thị Tâm"},
            {"AI23D019","Phạm Văn Thịnh"},{"AI23D020","Hoàng Thị Trinh"}
        };
        seedClass(ai23d, "AI23D");

        String[][] it23a = {
            {"IT23A001","Vũ Văn Bắc"},{"IT23A002","Đỗ Thị Cam"},{"IT23A003","Bùi Văn Cao"},
            {"IT23A004","Hồ Thị Cát"},{"IT23A005","Đặng Văn Chiến"},{"IT23A006","Ngô Thị Chinh"},
            {"IT23A007","Dương Văn Công"},{"IT23A008","Lý Thị Dần"},{"IT23A009","Phan Văn Duy"},
            {"IT23A010","Hà Thị Đào"},{"IT23A011","Nguyễn Văn Giáp"},{"IT23A012","Trần Thị Giỏi"},
            {"IT23A013","Lê Văn Hà"},{"IT23A014","Phạm Thị Hải"},{"IT23A015","Hoàng Văn Hán"},
            {"IT23A016","Vũ Thị Hiền"},{"IT23A017","Đỗ Văn Hoàn"},{"IT23A018","Bùi Thị Huệ"},
            {"IT23A019","Hồ Văn Huy"},{"IT23A020","Đặng Thị Huyền"},{"IT23A021","Ngô Văn Hướng"},
            {"IT23A022","Dương Thị Khanh"},{"IT23A023","Lý Văn Lân"},{"IT23A024","Phan Thị Lê"},
            {"IT23A025","Hà Văn Lương"},{"IT23A026","Nguyễn Thị Mến"},{"IT23A027","Trần Văn Mạnh"},
            {"IT23A028","Lê Thị Nụ"},{"IT23A029","Phạm Văn Nghiêm"},{"IT23A030","Hoàng Thị Nho"},
            {"IT23A031","Vũ Văn Niệm"},{"IT23A032","Đỗ Thị Nương"},{"IT23A033","Bùi Văn Phan"},
            {"IT23A034","Hồ Thị Quyên"},{"IT23A035","Đặng Văn Quốc"},{"IT23A036","Ngô Thị Phúc"},
            {"IT23A037","Dương Văn Sáng"},{"IT23A038","Lý Thị Thu"},{"IT23A039","Phan Văn Sĩ"},
            {"IT23A040","Hà Thị Tố"}
        };
        seedClass(it23a, "IT23A");
    }

    private void seedClass(String[][] data, String className) {
        for (String[] row : data) {
            String code = row[0];
            String name = row[1];
            Student s = studentRepo.save(Student.builder()
                    .studentCode(code).fullName(name)
                    .email(code.toLowerCase() + "@uni.edu.vn")
                    .className(className).isActive(true).build());
            accountRepo.save(Account.builder()
                    .username(code).password(code.toLowerCase())
                    .role("student").student(s).fullName(name).isActive(true).build());
        }
    }
}
