-- ============================================================
-- DỮ LIỆU MẪU - NHÓM 6
-- ============================================================
USE university_retake_db;
GO

-- Xóa dữ liệu cũ (theo thứ tự FK)
DELETE FROM course_registrations;
DELETE FROM grade_components;
DELETE FROM student_course_sections;
DELETE FROM equivalent_courses;
DELETE FROM course_sections;
DELETE FROM registration_periods;
DELETE FROM students;
DELETE FROM courses;
DELETE FROM semesters;
GO

-- ============================================================
-- HỌC KỲ
-- ============================================================
DECLARE @sem1_id UNIQUEIDENTIFIER = NEWID();
DECLARE @sem2_id UNIQUEIDENTIFIER = NEWID();
DECLARE @sem3_id UNIQUEIDENTIFIER = NEWID();

INSERT INTO semesters (id, code, name, academic_year, start_date, end_date, is_active) VALUES
(@sem1_id, 'HK1-2024', N'Học kỳ 1 - 2024-2025', '2024-2025', '2024-09-01', '2025-01-15', 1),
(@sem2_id, 'HK2-2024', N'Học kỳ 2 - 2024-2025', '2024-2025', '2025-02-01', '2025-06-30', 1),
(@sem3_id, 'HK1-2025', N'Học kỳ 1 - 2025-2026', '2025-2026', '2025-09-01', '2026-01-15', 1);

-- ============================================================
-- MÔN HỌC
-- ============================================================
DECLARE @c1 UNIQUEIDENTIFIER = NEWID();
DECLARE @c2 UNIQUEIDENTIFIER = NEWID();
DECLARE @c3 UNIQUEIDENTIFIER = NEWID();
DECLARE @c4 UNIQUEIDENTIFIER = NEWID();
DECLARE @c5 UNIQUEIDENTIFIER = NEWID();
DECLARE @c6 UNIQUEIDENTIFIER = NEWID();

INSERT INTO courses (id, code, name, credits, description, is_active) VALUES
(@c1, 'IT101', N'Nhập môn Lập trình', 3, N'Cơ bản về lập trình C', 1),
(@c2, 'IT102', N'Cấu trúc dữ liệu và Giải thuật', 4, N'DSA cơ bản', 1),
(@c3, 'IT201', N'Cơ sở dữ liệu', 3, N'SQL và thiết kế CSDL', 1),
(@c4, 'IT202', N'Lập trình hướng đối tượng', 3, N'OOP với Java', 1),
(@c5, 'IT301', N'Phát triển Web', 4, N'HTML/CSS/JS, Spring Boot', 1),
(@c6, 'MA101', N'Toán cao cấp 1', 3, N'Giải tích 1 biến', 1);

-- ============================================================
-- SINH VIÊN
-- ============================================================
DECLARE @s1 UNIQUEIDENTIFIER = NEWID();
DECLARE @s2 UNIQUEIDENTIFIER = NEWID();
DECLARE @s3 UNIQUEIDENTIFIER = NEWID();

INSERT INTO students (id, student_code, full_name, email, class_name, is_active) VALUES
(@s1, 'SV2024001', N'Nguyễn Văn An', 'an.nv@uni.edu.vn', 'CNTT-K17A', 1),
(@s2, 'SV2024002', N'Trần Thị Bình', 'binh.tt@uni.edu.vn', 'CNTT-K17A', 1),
(@s3, 'SV2024003', N'Lê Hoàng Cường', 'cuong.lh@uni.edu.vn', 'CNTT-K17B', 1);

-- ============================================================
-- LỚP HỌC PHẦN (course_sections)
-- ============================================================
DECLARE @cs1 UNIQUEIDENTIFIER = NEWID(); -- IT101 HK1
DECLARE @cs2 UNIQUEIDENTIFIER = NEWID(); -- IT102 HK1
DECLARE @cs3 UNIQUEIDENTIFIER = NEWID(); -- IT201 HK2
DECLARE @cs4 UNIQUEIDENTIFIER = NEWID(); -- IT101 HK2 (lớp học lại)
DECLARE @cs5 UNIQUEIDENTIFIER = NEWID(); -- IT102 HK2 (lớp học lại)
DECLARE @cs6 UNIQUEIDENTIFIER = NEWID(); -- IT202 HK1-2025
DECLARE @cs7 UNIQUEIDENTIFIER = NEWID(); -- MA101 HK1-2025
DECLARE @cs8 UNIQUEIDENTIFIER = NEWID(); -- IT301 HK1-2025

INSERT INTO course_sections (id, code, name, course_id, semester_id, max_students, current_students, class_type, status, registration_start, registration_end, is_active) VALUES
-- HK1-2024 (đã học xong)
(@cs1, 'IT101-01', N'IT101 - Lớp 01', @c1, @sem1_id, 50, 45, N'theory', 'closed', '2024-08-01', '2024-08-25', 1),
(@cs2, 'IT102-01', N'IT102 - Lớp 01', @c2, @sem1_id, 50, 48, N'theory', 'closed', '2024-08-01', '2024-08-25', 1),
-- HK2-2024 (đã học xong)
(@cs3, 'IT201-01', N'IT201 - Lớp 01', @c3, @sem2_id, 50, 50, N'theory', 'closed', '2025-01-15', '2025-01-30', 1),
-- HK2-2024 lớp học lại
(@cs4, 'IT101-RT01', N'IT101 - Lớp Học Lại', @c1, @sem2_id, 30, 5, N'Lớp học lại', 'open', '2025-01-15', '2025-01-30', 1),
(@cs5, 'IT102-RT01', N'IT102 - Lớp Học Lại', @c2, @sem2_id, 30, 3, N'Lớp học lại', 'open', '2025-01-15', '2025-01-30', 1),
-- HK1-2025 (đang mở đăng ký)
(@cs6, 'IT202-01', N'IT202 - Lớp 01', @c4, @sem3_id, 50, 20, N'theory', 'open', '2025-08-01', '2026-08-25', 1),
(@cs7, 'MA101-01', N'MA101 - Lớp 01', @c6, @sem3_id, 80, 60, N'theory', 'open', '2025-08-01', '2026-08-25', 1),
(@cs8, 'IT301-01', N'IT301 - Lớp 01', @c5, @sem3_id, 50, 25, N'hybrid', 'open', '2025-08-01', '2026-08-25', 1);

-- ============================================================
-- ĐĂNG KÝ HỌC PHẦN ĐÃ QUA (student_course_sections)
-- ============================================================
DECLARE @scs1 UNIQUEIDENTIFIER = NEWID(); -- An học IT101 - completed (rớt)
DECLARE @scs2 UNIQUEIDENTIFIER = NEWID(); -- An học IT102 - completed (đậu)
DECLARE @scs3 UNIQUEIDENTIFIER = NEWID(); -- An học IT201 - completed (đậu)
DECLARE @scs4 UNIQUEIDENTIFIER = NEWID(); -- Bình học IT101 - completed (rớt)
DECLARE @scs5 UNIQUEIDENTIFIER = NEWID(); -- Bình học IT102 - completed (rớt)

INSERT INTO student_course_sections (id, student_id, course_section_id, status, registered_at, note, is_active) VALUES
(@scs1, @s1, @cs1, 'completed', '2024-08-15', N'Đã thi xong', 1),
(@scs2, @s1, @cs2, 'completed', '2024-08-15', N'Đã thi xong', 1),
(@scs3, @s1, @cs3, 'completed', '2025-01-20', N'Đã thi xong', 1),
(@scs4, @s2, @cs1, 'completed', '2024-08-15', N'Đã thi xong', 1),
(@scs5, @s2, @cs2, 'completed', '2024-08-15', N'Đã thi xong', 1);

-- ============================================================
-- ĐIỂM THÀNH PHẦN (grade_components)
-- Quy đổi: tổng điểm < 5.0 = trượt -> phải học lại
-- ============================================================
-- An - IT101: tổng = 4.5 (TRƯỢT - cần học lại)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(@scs1, 'CC', N'Chuyên cần', 10.00, 8.0, 1),
(@scs1, 'GK', N'Giữa kỳ', 30.00, 4.0, 1),
(@scs1, 'CK', N'Cuối kỳ', 60.00, 4.0, 1);
-- Tổng = 0.1*8 + 0.3*4 + 0.6*4 = 0.8 + 1.2 + 2.4 = 4.4 < 5 -> RỚT

-- An - IT102: tổng = 7.5 (ĐẬU)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(@scs2, 'CC', N'Chuyên cần', 10.00, 9.0, 1),
(@scs2, 'GK', N'Giữa kỳ', 30.00, 7.0, 1),
(@scs2, 'CK', N'Cuối kỳ', 60.00, 7.5, 1);
-- Tổng = 0.9 + 2.1 + 4.5 = 7.5 -> ĐẬU

-- An - IT201: tổng = 6.5 (ĐẬU)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(@scs3, 'CC', N'Chuyên cần', 10.00, 8.0, 1),
(@scs3, 'GK', N'Giữa kỳ', 30.00, 6.0, 1),
(@scs3, 'CK', N'Cuối kỳ', 60.00, 6.5, 1);
-- Tổng = 0.8 + 1.8 + 3.9 = 6.5 -> ĐẬU

-- Bình - IT101: tổng = 3.0 (TRƯỢT)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(@scs4, 'CC', N'Chuyên cần', 10.00, 5.0, 1),
(@scs4, 'GK', N'Giữa kỳ', 30.00, 3.0, 1),
(@scs4, 'CK', N'Cuối kỳ', 60.00, 2.5, 1);
-- Tổng = 0.5 + 0.9 + 1.5 = 2.9 -> RỚT

-- Bình - IT102: tổng = 4.8 (TRƯỢT)
INSERT INTO grade_components (student_course_section_id, component_code, component_name, weight_percentage, score, is_active) VALUES
(@scs5, 'CC', N'Chuyên cần', 10.00, 7.0, 1),
(@scs5, 'GK', N'Giữa kỳ', 30.00, 5.0, 1),
(@scs5, 'CK', N'Cuối kỳ', 60.00, 4.5, 1);
-- Tổng = 0.7 + 1.5 + 2.7 = 4.9 -> RỚT

-- ============================================================
-- ĐỢT ĐĂNG KÝ
-- ============================================================
DECLARE @rp1 UNIQUEIDENTIFIER = NEWID();
DECLARE @rp2 UNIQUEIDENTIFIER = NEWID();

INSERT INTO registration_periods (id, name, semester_id, start_time, end_time, target_config, max_credits, min_credits, allow_retake, is_open, is_active) VALUES
(@rp1, N'Đợt 1 - HK1 2025-2026 (Học lại - Ưu tiên)', @sem3_id, '2025-08-01', '2026-08-10', '{"target":"retake_only"}', 25, 0, 1, 1, 1),
(@rp2, N'Đợt 2 - HK1 2025-2026 (Học phần)', @sem3_id, '2026-08-11', '2026-08-25', '{"target":"all"}', 25, 12, 1, 1, 1);

-- ============================================================
-- MÔN TƯƠNG ĐƯƠNG
-- ============================================================
INSERT INTO equivalent_courses (original_course_id, equivalent_course_id, equivalence_type, effect_date, note, is_active) VALUES
(@c1, @c2, 2, '2024-01-01', N'IT101 và IT102 có thể học thay thế trong một số trường hợp', 1);

PRINT N'Đã chèn dữ liệu mẫu thành công!';
PRINT N'==> Sinh viên SV2024001 (An) trượt môn IT101 -> cần học lại';
PRINT N'==> Sinh viên SV2024002 (Bình) trượt môn IT101 và IT102 -> cần học lại';
GO

-- Kiểm tra dữ liệu
SELECT TOP 10 * FROM students;
SELECT TOP 10 * FROM courses;
SELECT TOP 10 * FROM course_sections;
SELECT TOP 10 * FROM student_course_sections;
SELECT TOP 10 * FROM grade_components;
GO
