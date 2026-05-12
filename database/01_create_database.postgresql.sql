-- ============================================================
-- NHÓM 6: ĐĂNG KÝ HỌC LẠI - ĐĂNG KÝ HỌC PHẦN
-- Database: PostgreSQL
-- ============================================================

-- Bật extension uuid
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================================
-- 1. Bảng courses
-- ============================================================
DROP TABLE IF EXISTS equivalent_courses CASCADE;
DROP TABLE IF EXISTS course_registrations CASCADE;
DROP TABLE IF EXISTS grade_components CASCADE;
DROP TABLE IF EXISTS student_course_sections CASCADE;
DROP TABLE IF EXISTS registration_periods CASCADE;
DROP TABLE IF EXISTS course_sections CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS semesters CASCADE;
DROP TABLE IF EXISTS courses CASCADE;

CREATE TABLE courses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    credits INT NOT NULL,
    description VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE
);

-- ============================================================
-- 2. Bảng students
-- ============================================================
CREATE TABLE students (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_code VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    class_name VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE
);

-- ============================================================
-- 3. Bảng semesters
-- ============================================================
CREATE TABLE semesters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    academic_year VARCHAR(20),
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE
);

-- ============================================================
-- 4. Bảng course_sections
-- ============================================================
CREATE TABLE course_sections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(100) NOT NULL,
    name VARCHAR(255),
    course_id UUID NOT NULL,
    semester_id UUID NOT NULL,
    max_students INT DEFAULT 50,
    min_students INT DEFAULT 10,
    current_students INT DEFAULT 0,
    class_type VARCHAR(255) DEFAULT 'theory',
    status VARCHAR(50) DEFAULT 'open',
    registration_start TIMESTAMP,
    registration_end TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT FK_section_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT FK_section_semester FOREIGN KEY (semester_id) REFERENCES semesters(id)
);

-- ============================================================
-- 5. Bảng student_course_sections
-- ============================================================
CREATE TABLE student_course_sections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID NOT NULL,
    course_section_id UUID NOT NULL,
    status VARCHAR(50) DEFAULT 'studying',
    registered_at TIMESTAMP DEFAULT NOW(),
    note VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT FK_scs_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT FK_scs_section FOREIGN KEY (course_section_id) REFERENCES course_sections(id)
);

-- ============================================================
-- 6. Bảng grade_components
-- ============================================================
CREATE TABLE grade_components (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_course_section_id UUID NOT NULL,
    component_code VARCHAR(20),
    component_name VARCHAR(50),
    weight_percentage DECIMAL(5,2),
    score DECIMAL(4,2),
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT FK_grade_scs FOREIGN KEY (student_course_section_id) REFERENCES student_course_sections(id)
);

-- ============================================================
-- 7. Bảng registration_periods
-- ============================================================
CREATE TABLE registration_periods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    semester_id UUID NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    target_config TEXT,
    max_credits SMALLINT DEFAULT 25,
    min_credits SMALLINT DEFAULT 12,
    allow_retake BOOLEAN DEFAULT TRUE,
    is_open BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT FK_regperiod_semester FOREIGN KEY (semester_id) REFERENCES semesters(id)
);

-- ============================================================
-- 8. Bảng course_registrations
-- ============================================================
CREATE TABLE course_registrations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID NOT NULL,
    course_section_id UUID NOT NULL,
    registration_period_id UUID NOT NULL,
    registration_type VARCHAR(20) DEFAULT 'NEW',
    status VARCHAR(20) DEFAULT 'PENDING',
    registered_at TIMESTAMP DEFAULT NOW(),
    note VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT FK_reg_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT FK_reg_section FOREIGN KEY (course_section_id) REFERENCES course_sections(id),
    CONSTRAINT FK_reg_period FOREIGN KEY (registration_period_id) REFERENCES registration_periods(id)
);

-- ============================================================
-- 9. Bảng equivalent_courses
-- ============================================================
CREATE TABLE equivalent_courses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    original_course_id UUID NOT NULL,
    equivalent_course_id UUID NOT NULL,
    equivalence_type SMALLINT DEFAULT 1,
    effect_date DATE DEFAULT NOW(),
    note VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT FK_equiv_original FOREIGN KEY (original_course_id) REFERENCES courses(id),
    CONSTRAINT FK_equiv_equivalent FOREIGN KEY (equivalent_course_id) REFERENCES courses(id)
);

-- ============================================================
-- 10. Bảng accounts
-- ============================================================
CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'student',
    student_id UUID NULL,
    full_name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT UQ_accounts_username UNIQUE (username),
    CONSTRAINT FK_accounts_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT CK_accounts_role CHECK (role IN ('student', 'admin'))
);
