-- ============================================================
-- NHÓM 6: ĐĂNG KÝ HỌC LẠI - ĐĂNG KÝ HỌC PHẦN
-- Database: SQL Server
-- ============================================================

-- Tạo database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'university_retake_db')
BEGIN
    CREATE DATABASE university_retake_db;
END
GO

USE university_retake_db;
GO

-- ============================================================
-- 1. Bảng courses (Môn học) - tham chiếu từ Nhóm 4
-- ============================================================
IF OBJECT_ID('courses', 'U') IS NOT NULL DROP TABLE courses;
GO
CREATE TABLE courses (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    code VARCHAR(20) NOT NULL UNIQUE,
    name NVARCHAR(255) NOT NULL,
    credits INT NOT NULL,
    description NVARCHAR(500),
    is_active BIT DEFAULT 1
);
GO

-- ============================================================
-- 2. Bảng students (Sinh viên) - tham chiếu từ Nhóm 3
-- ============================================================
IF OBJECT_ID('students', 'U') IS NOT NULL DROP TABLE students;
GO
CREATE TABLE students (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    student_code VARCHAR(20) NOT NULL UNIQUE,
    full_name NVARCHAR(255) NOT NULL,
    email VARCHAR(100),
    class_name NVARCHAR(50),
    is_active BIT DEFAULT 1
);
GO

-- ============================================================
-- 3. Bảng semesters (Học kỳ) - tham chiếu từ Nhóm 5
-- ============================================================
IF OBJECT_ID('semesters', 'U') IS NOT NULL DROP TABLE semesters;
GO
CREATE TABLE semesters (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    code VARCHAR(20) NOT NULL,
    name NVARCHAR(100) NOT NULL,
    academic_year VARCHAR(20),
    start_date DATE,
    end_date DATE,
    is_active BIT DEFAULT 1
);
GO

-- ============================================================
-- 4. Bảng course_sections (Lớp học phần) - tham chiếu Nhóm 5
-- ============================================================
IF OBJECT_ID('course_sections', 'U') IS NOT NULL DROP TABLE course_sections;
GO
CREATE TABLE course_sections (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    code VARCHAR(100) NOT NULL,
    name NVARCHAR(255),
    course_id UNIQUEIDENTIFIER NOT NULL,
    semester_id UNIQUEIDENTIFIER NOT NULL,
    max_students INT DEFAULT 50,
    min_students INT DEFAULT 10,
    current_students INT DEFAULT 0,
    class_type NVARCHAR(255) DEFAULT N'theory', -- theory / lab / hybrid / Lớp học lại
    status VARCHAR(50) DEFAULT 'open', -- planned / open / closed / canceled
    registration_start DATETIME2,
    registration_end DATETIME2,
    is_active BIT DEFAULT 1,
    CONSTRAINT FK_section_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT FK_section_semester FOREIGN KEY (semester_id) REFERENCES semesters(id)
);
GO

-- ============================================================
-- 5. Bảng student_course_sections (SV trong lớp học phần) - Nhóm 5
-- ============================================================
IF OBJECT_ID('student_course_sections', 'U') IS NOT NULL DROP TABLE student_course_sections;
GO
CREATE TABLE student_course_sections (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    student_id UNIQUEIDENTIFIER NOT NULL,
    course_section_id UNIQUEIDENTIFIER NOT NULL,
    status VARCHAR(50) DEFAULT 'studying', -- studying, completed, dropped
    registered_at DATETIME DEFAULT GETDATE(),
    note NVARCHAR(255),
    is_active BIT DEFAULT 1,
    CONSTRAINT FK_scs_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT FK_scs_section FOREIGN KEY (course_section_id) REFERENCES course_sections(id)
);
GO

-- ============================================================
-- 6. Bảng grade_components (Điểm thành phần) - Nhóm 8
-- Dùng để xác định môn pass/fail (điểm tổng kết)
-- ============================================================
IF OBJECT_ID('grade_components', 'U') IS NOT NULL DROP TABLE grade_components;
GO
CREATE TABLE grade_components (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    student_course_section_id UNIQUEIDENTIFIER NOT NULL,
    component_code VARCHAR(20), -- CC, KTTX, GK, CK
    component_name NVARCHAR(50),
    weight_percentage DECIMAL(5,2),
    score DECIMAL(4,2),
    is_active BIT DEFAULT 1,
    CONSTRAINT FK_grade_scs FOREIGN KEY (student_course_section_id) REFERENCES student_course_sections(id)
);
GO

-- ============================================================
-- 7. Bảng registration_periods (Đợt đăng ký)
-- ============================================================
IF OBJECT_ID('registration_periods', 'U') IS NOT NULL DROP TABLE registration_periods;
GO
CREATE TABLE registration_periods (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    name NVARCHAR(200) NOT NULL,
    semester_id UNIQUEIDENTIFIER NOT NULL,
    start_time DATETIME2 NOT NULL,
    end_time DATETIME2 NOT NULL,
    target_config NVARCHAR(MAX), -- JSON
    max_credits TINYINT DEFAULT 25,
    min_credits TINYINT DEFAULT 12,
    allow_retake BIT DEFAULT 1,
    is_open BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    is_active BIT DEFAULT 1,
    CONSTRAINT FK_regperiod_semester FOREIGN KEY (semester_id) REFERENCES semesters(id)
);
GO

-- ============================================================
-- 8. Bảng course_registrations (Chi tiết đăng ký)
-- ============================================================
IF OBJECT_ID('course_registrations', 'U') IS NOT NULL DROP TABLE course_registrations;
GO
CREATE TABLE course_registrations (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    student_id UNIQUEIDENTIFIER NOT NULL,
    course_section_id UNIQUEIDENTIFIER NOT NULL,
    registration_period_id UNIQUEIDENTIFIER NOT NULL,
    registration_type VARCHAR(20) DEFAULT 'NEW', -- NEW, RETAKE, IMPROVE
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED, CANCELED
    registered_at DATETIME2 DEFAULT GETDATE(),
    note NVARCHAR(500),
    is_active BIT DEFAULT 1,
    CONSTRAINT FK_reg_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT FK_reg_section FOREIGN KEY (course_section_id) REFERENCES course_sections(id),
    CONSTRAINT FK_reg_period FOREIGN KEY (registration_period_id) REFERENCES registration_periods(id)
);
GO

-- ============================================================
-- 9. Bảng equivalent_courses (Môn tương đương)
-- ============================================================
IF OBJECT_ID('equivalent_courses', 'U') IS NOT NULL DROP TABLE equivalent_courses;
GO
CREATE TABLE equivalent_courses (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    original_course_id UNIQUEIDENTIFIER NOT NULL, -- Môn cũ
    equivalent_course_id UNIQUEIDENTIFIER NOT NULL, -- Môn tương đương
    equivalence_type TINYINT DEFAULT 1, -- 1: Thay thế hoàn toàn, 2: Tương đương song song
    effect_date DATE DEFAULT GETDATE(),
    note NVARCHAR(500),
    is_active BIT DEFAULT 1,
    CONSTRAINT FK_equiv_original FOREIGN KEY (original_course_id) REFERENCES courses(id),
    CONSTRAINT FK_equiv_equivalent FOREIGN KEY (equivalent_course_id) REFERENCES courses(id)
);
GO

PRINT N'Tạo database và bảng thành công!';
