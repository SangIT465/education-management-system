-- ============================================================
-- MIGRATION 04: Thêm dữ liệu 5 lớp sinh viên (ST23A, LM23A, EL23T, AI23D, IT23A)
-- Chạy sau khi đã chạy 01, 02, 03
-- ============================================================
USE university_retake_db;
GO

-- ============================================================
-- LỚP ST23A - Công nghệ Phần mềm 2023A (30 sinh viên)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'ST23A001')
INSERT INTO students (id, student_code, full_name, email, class_name, is_active) VALUES
(NEWID(), 'ST23A001', N'Nguyễn Văn An',     'st23a001@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A002', N'Trần Thị Anh',      'st23a002@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A003', N'Lê Văn Bình',       'st23a003@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A004', N'Phạm Thị Bảo',      'st23a004@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A005', N'Hoàng Văn Cường',   'st23a005@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A006', N'Vũ Thị Chi',        'st23a006@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A007', N'Đỗ Văn Dũng',       'st23a007@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A008', N'Bùi Thị Diễm',      'st23a008@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A009', N'Hồ Văn Đạt',        'st23a009@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A010', N'Đặng Thị Giang',    'st23a010@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A011', N'Ngô Văn Hải',       'st23a011@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A012', N'Dương Thị Hằng',    'st23a012@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A013', N'Lý Văn Hùng',       'st23a013@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A014', N'Phan Thị Hoa',      'st23a014@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A015', N'Hà Văn Khoa',       'st23a015@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A016', N'Nguyễn Thị Hương',  'st23a016@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A017', N'Trần Văn Lâm',      'st23a017@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A018', N'Lê Thị Lan',        'st23a018@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A019', N'Phạm Văn Long',     'st23a019@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A020', N'Hoàng Thị Linh',    'st23a020@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A021', N'Vũ Văn Minh',       'st23a021@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A022', N'Đỗ Thị Mai',        'st23a022@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A023', N'Bùi Văn Nam',       'st23a023@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A024', N'Hồ Thị Ngọc',       'st23a024@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A025', N'Đặng Văn Nhân',     'st23a025@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A026', N'Ngô Thị Như',       'st23a026@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A027', N'Dương Văn Phúc',    'st23a027@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A028', N'Lý Thị Phương',     'st23a028@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A029', N'Phan Văn Quân',     'st23a029@uni.edu.vn', 'ST23A', 1),
(NEWID(), 'ST23A030', N'Hà Thị Thảo',       'st23a030@uni.edu.vn', 'ST23A', 1);
GO

-- ============================================================
-- LỚP LM23A - Logistics & Quản lý 2023A (25 sinh viên)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'LM23A001')
INSERT INTO students (id, student_code, full_name, email, class_name, is_active) VALUES
(NEWID(), 'LM23A001', N'Nguyễn Văn Sơn',    'lm23a001@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A002', N'Trần Thị Trang',    'lm23a002@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A003', N'Lê Văn Thắng',      'lm23a003@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A004', N'Phạm Thị Thư',      'lm23a004@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A005', N'Hoàng Văn Toàn',    'lm23a005@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A006', N'Vũ Thị Uyên',       'lm23a006@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A007', N'Đỗ Văn Trung',      'lm23a007@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A008', N'Bùi Thị Vân',       'lm23a008@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A009', N'Hồ Văn Tuấn',       'lm23a009@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A010', N'Đặng Thị Yến',      'lm23a010@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A011', N'Ngô Văn Tài',       'lm23a011@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A012', N'Dương Thị Liên',    'lm23a012@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A013', N'Lý Văn Việt',       'lm23a013@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A014', N'Phan Thị Nhi',      'lm23a014@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A015', N'Hà Văn Dương',      'lm23a015@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A016', N'Nguyễn Thị Hiền',   'lm23a016@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A017', N'Trần Văn Khải',     'lm23a017@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A018', N'Lê Thị Loan',       'lm23a018@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A019', N'Phạm Văn Hậu',      'lm23a019@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A020', N'Hoàng Thị Mỹ',      'lm23a020@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A021', N'Vũ Văn Điền',       'lm23a021@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A022', N'Đỗ Thị Nga',        'lm23a022@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A023', N'Bùi Văn Giang',     'lm23a023@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A024', N'Hồ Thị Kim',        'lm23a024@uni.edu.vn', 'LM23A', 1),
(NEWID(), 'LM23A025', N'Đặng Văn Hưng',     'lm23a025@uni.edu.vn', 'LM23A', 1);
GO

-- ============================================================
-- LỚP EL23T - Điện tử 2023T (35 sinh viên)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'EL23T001')
INSERT INTO students (id, student_code, full_name, email, class_name, is_active) VALUES
(NEWID(), 'EL23T001', N'Ngô Văn Bảo',       'el23t001@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T002', N'Dương Thị Bình',    'el23t002@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T003', N'Lý Văn Chiến',      'el23t003@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T004', N'Phan Thị Cẩm',      'el23t004@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T005', N'Hà Văn Công',       'el23t005@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T006', N'Nguyễn Thị Diệu',   'el23t006@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T007', N'Trần Văn Đông',     'el23t007@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T008', N'Lê Thị Đoan',       'el23t008@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T009', N'Phạm Văn Hào',      'el23t009@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T010', N'Hoàng Thị Hạnh',    'el23t010@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T011', N'Vũ Văn Hiếu',       'el23t011@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T012', N'Đỗ Thị Huệ',        'el23t012@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T013', N'Bùi Văn Khánh',     'el23t013@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T014', N'Hồ Thị Khanh',      'el23t014@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T015', N'Đặng Văn Kiên',     'el23t015@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T016', N'Ngô Thị Lê',        'el23t016@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T017', N'Dương Văn Lực',     'el23t017@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T018', N'Lý Thị Lộc',        'el23t018@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T019', N'Phan Văn Mạnh',     'el23t019@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T020', N'Hà Thị Minh',       'el23t020@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T021', N'Nguyễn Văn Nghĩa',  'el23t021@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T022', N'Trần Thị Nhung',    'el23t022@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T023', N'Lê Văn Phong',      'el23t023@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T024', N'Phạm Thị Quỳnh',    'el23t024@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T025', N'Hoàng Văn Quý',     'el23t025@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T026', N'Vũ Thị Sương',      'el23t026@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T027', N'Đỗ Văn Tâm',        'el23t027@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T028', N'Bùi Thị Thúy',      'el23t028@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T029', N'Hồ Văn Thiện',      'el23t029@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T030', N'Đặng Thị Thu',      'el23t030@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T031', N'Ngô Văn Thuận',     'el23t031@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T032', N'Dương Thị Tuyền',   'el23t032@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T033', N'Lý Văn Vinh',       'el23t033@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T034', N'Phan Thị Xuân',     'el23t034@uni.edu.vn', 'EL23T', 1),
(NEWID(), 'EL23T035', N'Hà Văn Định',       'el23t035@uni.edu.vn', 'EL23T', 1);
GO

-- ============================================================
-- LỚP AI23D - Trí tuệ Nhân tạo 2023D (20 sinh viên)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'AI23D001')
INSERT INTO students (id, student_code, full_name, email, class_name, is_active) VALUES
(NEWID(), 'AI23D001', N'Nguyễn Văn Anh',    'ai23d001@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D002', N'Trần Thị Châu',     'ai23d002@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D003', N'Lê Văn Chính',      'ai23d003@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D004', N'Phạm Thị Dung',     'ai23d004@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D005', N'Hoàng Văn Đức',     'ai23d005@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D006', N'Vũ Thị Thủy',       'ai23d006@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D007', N'Đỗ Văn Hưng',       'ai23d007@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D008', N'Bùi Thị Huyền',     'ai23d008@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D009', N'Hồ Văn Khang',      'ai23d009@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D010', N'Đặng Thị Khánh',    'ai23d010@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D011', N'Ngô Văn Lợi',       'ai23d011@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D012', N'Dương Thị Luyến',   'ai23d012@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D013', N'Lý Văn Ninh',       'ai23d013@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D014', N'Phan Thị Oanh',     'ai23d014@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D015', N'Hà Văn Phát',       'ai23d015@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D016', N'Nguyễn Thị Quyên',  'ai23d016@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D017', N'Trần Văn Sang',     'ai23d017@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D018', N'Lê Thị Tâm',        'ai23d018@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D019', N'Phạm Văn Thịnh',    'ai23d019@uni.edu.vn', 'AI23D', 1),
(NEWID(), 'AI23D020', N'Hoàng Thị Trinh',   'ai23d020@uni.edu.vn', 'AI23D', 1);
GO

-- ============================================================
-- LỚP IT23A - Công nghệ Thông tin 2023A (40 sinh viên)
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM students WHERE student_code = 'IT23A001')
INSERT INTO students (id, student_code, full_name, email, class_name, is_active) VALUES
(NEWID(), 'IT23A001', N'Vũ Văn Bắc',        'it23a001@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A002', N'Đỗ Thị Cam',        'it23a002@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A003', N'Bùi Văn Cao',       'it23a003@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A004', N'Hồ Thị Cát',        'it23a004@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A005', N'Đặng Văn Chiến',    'it23a005@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A006', N'Ngô Thị Chinh',     'it23a006@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A007', N'Dương Văn Công',    'it23a007@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A008', N'Lý Thị Dần',        'it23a008@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A009', N'Phan Văn Duy',      'it23a009@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A010', N'Hà Thị Đào',        'it23a010@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A011', N'Nguyễn Văn Giáp',   'it23a011@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A012', N'Trần Thị Giỏi',     'it23a012@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A013', N'Lê Văn Hà',         'it23a013@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A014', N'Phạm Thị Hải',      'it23a014@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A015', N'Hoàng Văn Hán',     'it23a015@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A016', N'Vũ Thị Hiền',       'it23a016@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A017', N'Đỗ Văn Hoàn',       'it23a017@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A018', N'Bùi Thị Huệ',       'it23a018@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A019', N'Hồ Văn Huy',        'it23a019@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A020', N'Đặng Thị Huyền',    'it23a020@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A021', N'Ngô Văn Hướng',     'it23a021@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A022', N'Dương Thị Khanh',   'it23a022@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A023', N'Lý Văn Lân',        'it23a023@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A024', N'Phan Thị Lê',       'it23a024@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A025', N'Hà Văn Lương',      'it23a025@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A026', N'Nguyễn Thị Mến',    'it23a026@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A027', N'Trần Văn Mạnh',     'it23a027@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A028', N'Lê Thị Nụ',         'it23a028@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A029', N'Phạm Văn Nghiêm',   'it23a029@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A030', N'Hoàng Thị Nho',     'it23a030@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A031', N'Vũ Văn Niệm',       'it23a031@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A032', N'Đỗ Thị Nương',      'it23a032@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A033', N'Bùi Văn Phan',      'it23a033@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A034', N'Hồ Thị Quyên',      'it23a034@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A035', N'Đặng Văn Quốc',     'it23a035@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A036', N'Ngô Thị Phúc',      'it23a036@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A037', N'Dương Văn Sáng',    'it23a037@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A038', N'Lý Thị Thu',        'it23a038@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A039', N'Phan Văn Sĩ',       'it23a039@uni.edu.vn', 'IT23A', 1),
(NEWID(), 'IT23A040', N'Hà Thị Tố',         'it23a040@uni.edu.vn', 'IT23A', 1);
GO

-- ============================================================
-- TẠO TÀI KHOẢN cho tất cả sinh viên lớp mới (password = mã SV viết thường)
-- ============================================================
INSERT INTO accounts (username, password, role, student_id, full_name, is_active)
SELECT
    s.student_code,
    LOWER(s.student_code),
    'student',
    s.id,
    s.full_name,
    1
FROM students s
WHERE s.class_name IN ('ST23A', 'LM23A', 'EL23T', 'AI23D', 'IT23A')
  AND NOT EXISTS (
    SELECT 1 FROM accounts a WHERE a.username = s.student_code
  );
GO

-- Kiểm tra kết quả
SELECT class_name, COUNT(*) AS so_sv
FROM students
WHERE class_name IN ('ST23A', 'LM23A', 'EL23T', 'AI23D', 'IT23A')
GROUP BY class_name
ORDER BY class_name;

PRINT N'=== Migration 04 hoàn thành ===';
PRINT N'Mật khẩu sinh viên = mã SV viết thường (VD: ST23A001 -> st23a001)';
GO
