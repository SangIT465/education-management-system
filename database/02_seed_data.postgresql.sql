-- ============================================================
-- DỮ LIỆU MẪU - NHÓM 6 (PostgreSQL)
-- ============================================================

DO $$
DECLARE
    sem1_id UUID := gen_random_uuid();
    sem2_id UUID := gen_random_uuid();
    sem3_id UUID := gen_random_uuid();
    c1 UUID := gen_random_uuid();
    c2 UUID := gen_random_uuid();
    c3 UUID := gen_random_uuid();
    c4 UUID := gen_random_uuid();
    c5 UUID := gen_random_uuid();
    c6 UUID := gen_random_uuid();
    s1 UUID := gen_random_uuid();
    s2 UUID := gen_random_uuid();
    s3 UUID := gen_random_uuid();
    cs1 UUID := gen_random_uuid();
    cs2 UUID := gen_random_uuid();
    cs3 UUID := gen_random_uuid();
    cs4 UUID := gen_random_uuid();
    cs5 UUID := gen_random_uuid();
    cs6 UUID := gen_random_uuid();
    cs7 UUID := gen_random_uuid();
    cs8 UUID := gen_random_uuid();
    scs1 UUID := gen_random_uuid();
    scs2 UUID := gen_random_uuid();
    scs3 UUID := gen_random_uuid();
    scs4 UUID := gen_random_uuid();
    scs5 UUID := gen_random_uuid();
    rp1 UUID := gen_random_uuid();
    rp2 UUID := gen_random_uuid();
BEGIN

-- Xóa dữ liệu cũ
DELETE FROM course_registrations;
DELETE FROM grade_components;
DELETE FROM student_course_sections;
DELETE FROM equivalent_courses;
DELETE FROM course_sections;
DELETE FROM registration_periods;
DELETE FROM accounts;
DELETE FROM students;
DELETE FROM courses;
DELETE FROM semesters;

-- HỌC KỲ
INSERT INTO semesters (id, code, name, academic_year, start_date, end_date, is_active) VALUES
(sem1_id, 'HK1-2024', 'Học kỳ 1 - 2024-2025', '2024-2025', '2024-09-01', '2025-01-15', TRUE),
(sem2_id, 'HK2-2024', 'Học kỳ 2 - 2024-2025', '2024-2025', '2025-02-01', '2025-06-30', TRUE),
(sem3_id, 'HK1-2025', 'Học kỳ 1 - 2025-2026', '2025-2026', '2025-09-01', '2026-01-15', TRUE);

-- MÔN HỌC
INSERT INTO courses (id, code, name, credits, description, is_active) VALUES
(c1, 'IT101', 'Nhập môn Lập trình', 3, 'Cơ bản về lập trình C', TRUE),
(c2, 'IT102', 'Cấu trúc dữ liệu và Giải thuật', 4, 'DSA cơ bản', TRUE),
(c3, 'IT201', 'Cơ sở dữ liệu', 3, 'SQL và thiết kế CSDL', TRUE),
(c4, 'IT202', 'Lập trình hướng đối tượng', 3, 'OOP với Java', TRUE),
(c5, 'IT301', 'Phát triển Web', 4, 'HTML/CSS/JS, Spring Boot', TRUE),
(c6, 'MA101', 'Toán cao cấp 1', 3, 'Giải tích 1 biến', TRUE);

-- SINH VIÊN
INSERT INTO students (id, student_code, full_name, email, class_name, is_active) VALUES
(s1, 'SV2024001', 'Nguyễn Văn An', 'an.nv@uni.edu.vn', 'CNTT-K17A', TRUE),
(s2, 'SV2024002', 'Trần Thị Bình', 'binh.tt@uni.edu.vn', 'CNTT-K17A', TRUE),
(s3, 'SV2024003', 'Lê Hoàng Cường', 'cuong.lh@uni.edu.vn', 'CNTT-K17B', TRUE);

-- LỚP HỌC PHẦN
INSERT INTO course_sections (id, code, name, course_id, semester_id, max_students, current_students, class_type, status, registration_start, registration_end, is_active) VALUES
(cs1, 'IT101-01', 'IT101 - Lớp 01', c1, sem1_id, 50, 45, 'theory', 'closed', '2024-08-01', '2024-08-25', TRUE),
(cs2, 'IT102-01', 'IT102 - Lớp 01', c2, sem1_id, 50, 48, 'theory', 'closed', '2024-08-01', '2024-08-25', TRUE),
(cs3, 'IT201-01', 'IT201 - Lớp 01', c3, sem2_id, 50, 50, 'theory', 'closed', '2025-01-15', '2025-01-30', TRUE),
(cs4, 'IT101-RT01', 'IT101 - Lớp Học Lại', c1, sem2_id, 30, 5, 'Lớp học lại', 'open', '2025-01-15', '2025-01-30', TRUE),
(cs5, 'IT102-RT01', 'IT102 - Lớp Học Lại', c2, sem2_id, 30, 3, 'Lớp học lại', 'open', '2025-01-15', '2025-01-30', TRUE),
(cs6, 'IT202-01', 'IT202 - Lớp 01', c4, sem3_id, 50, 20, 'theory', 'open', '2025-08-01', '2026-08-25', TRUE),
(cs7, 'MA101-01', 'MA101 - Lớp 01', c6, sem3_id, 80, 60, 'theory', 'open', '2025-08-01', '2026-08-25', TRUE),
(cs8, 'IT301-01', 'IT301 - Lớp 01', c5, sem3_id, 50, 25, 'hybrid', 'open', '2025-08-01', '2026-08-25', TRUE);

-- ĐĂNG KÝ HỌC PHẦN ĐÃ QUA
INSERT INTO student_course_sections (id, student_id, course_section_id, status, registered_at, note, is_active) VALUES
(scs1, s1, cs1, 'completed', '2024-08-15', 'Đã thi xong', TRUE),
(scs2, s1, cs2, 'completed', '2024-08-15', 'Đã thi xong', TRUE),
(scs3, s1, cs3, 'completed', '2025-01-20', 'Đã thi xong', TRUE),
(scs4, s2, cs1, 'completed', '2024-08-15', 'Đã thi xong', TRUE),
(scs5, s2, cs2, 'completed', '2024-08-15', 'Đã thi xong', TRUE);

-- ĐIỂM THÀNH PHẦN
-- An - IT101: RỚT (4.4 < 5)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(scs1, 'CC', 'Chuyên cần', 10.00, 8.0, TRUE),
(scs1, 'GK', 'Giữa kỳ', 30.00, 4.0, TRUE),
(scs1, 'CK', 'Cuối kỳ', 60.00, 4.0, TRUE);

-- An - IT102: ĐẬU (7.5)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(scs2, 'CC', 'Chuyên cần', 10.00, 9.0, TRUE),
(scs2, 'GK', 'Giữa kỳ', 30.00, 7.0, TRUE),
(scs2, 'CK', 'Cuối kỳ', 60.00, 7.5, TRUE);

-- An - IT201: ĐẬU (6.5)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(scs3, 'CC', 'Chuyên cần', 10.00, 8.0, TRUE),
(scs3, 'GK', 'Giữa kỳ', 30.00, 6.0, TRUE),
(scs3, 'CK', 'Cuối kỳ', 60.00, 6.5, TRUE);

-- Bình - IT101: RỚT (2.9)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(scs4, 'CC', 'Chuyên cần', 10.00, 5.0, TRUE),
(scs4, 'GK', 'Giữa kỳ', 30.00, 3.0, TRUE),
(scs4, 'CK', 'Cuối kỳ', 60.00, 2.5, TRUE);

-- Bình - IT102: RỚT (4.9)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(scs5, 'CC', 'Chuyên cần', 10.00, 7.0, TRUE),
(scs5, 'GK', 'Giữa kỳ', 30.00, 5.0, TRUE),
(scs5, 'CK', 'Cuối kỳ', 60.00, 4.5, TRUE);

-- ĐỢT ĐĂNG KÝ
INSERT INTO registration_periods (id, name, semester_id, start_time, end_time, target_config, max_credits, min_credits, allow_retake, is_open, is_active) VALUES
(rp1, 'Đợt 1 - HK1 2025-2026 (Học lại - Ưu tiên)', sem3_id, '2025-08-01', '2026-08-10', '{"target":"retake_only"}', 25, 0, TRUE, TRUE, TRUE),
(rp2, 'Đợt 2 - HK1 2025-2026 (Học phần)', sem3_id, '2026-08-11', '2026-08-25', '{"target":"all"}', 25, 12, TRUE, TRUE, TRUE);

-- MÔN TƯƠNG ĐƯƠNG
INSERT INTO equivalent_courses (original_course_id, equivalent_course_id, equivalence_type, effect_date, note, is_active) VALUES
(c1, c2, 2, '2024-01-01', 'IT101 và IT102 có thể học thay thế trong một số trường hợp', TRUE);

-- TÀI KHOẢN
INSERT INTO accounts (username, password, role, full_name, is_active) VALUES
('admin', 'admin123', 'admin', 'Quản trị viên', TRUE)
ON CONFLICT (username) DO NOTHING;

INSERT INTO accounts (username, password, role, student_id, full_name, is_active)
SELECT 'SV2024001', 'sv001', 'student', id, full_name, TRUE FROM students WHERE student_code = 'SV2024001'
ON CONFLICT (username) DO NOTHING;

INSERT INTO accounts (username, password, role, student_id, full_name, is_active)
SELECT 'SV2024002', 'sv002', 'student', id, full_name, TRUE FROM students WHERE student_code = 'SV2024002'
ON CONFLICT (username) DO NOTHING;

INSERT INTO accounts (username, password, role, student_id, full_name, is_active)
SELECT 'SV2024003', 'sv003', 'student', id, full_name, TRUE FROM students WHERE student_code = 'SV2024003'
ON CONFLICT (username) DO NOTHING;

RAISE NOTICE 'Đã chèn dữ liệu mẫu thành công!';
END $$;
