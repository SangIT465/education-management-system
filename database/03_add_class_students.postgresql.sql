-- ============================================================
-- MIGRATION: Thêm dữ liệu 5 lớp sinh viên (PostgreSQL)
-- ============================================================

-- LỚP ST23A
INSERT INTO students (id, student_code, full_name, email, class_name, is_active)
SELECT gen_random_uuid(), t.code, t.name, t.email, 'ST23A', TRUE
FROM (VALUES
('ST23A001','Nguyễn Văn An','st23a001@uni.edu.vn'),('ST23A002','Trần Thị Anh','st23a002@uni.edu.vn'),
('ST23A003','Lê Văn Bình','st23a003@uni.edu.vn'),('ST23A004','Phạm Thị Bảo','st23a004@uni.edu.vn'),
('ST23A005','Hoàng Văn Cường','st23a005@uni.edu.vn'),('ST23A006','Vũ Thị Chi','st23a006@uni.edu.vn'),
('ST23A007','Đỗ Văn Dũng','st23a007@uni.edu.vn'),('ST23A008','Bùi Thị Diễm','st23a008@uni.edu.vn'),
('ST23A009','Hồ Văn Đạt','st23a009@uni.edu.vn'),('ST23A010','Đặng Thị Giang','st23a010@uni.edu.vn'),
('ST23A011','Ngô Văn Hải','st23a011@uni.edu.vn'),('ST23A012','Dương Thị Hằng','st23a012@uni.edu.vn'),
('ST23A013','Lý Văn Hùng','st23a013@uni.edu.vn'),('ST23A014','Phan Thị Hoa','st23a014@uni.edu.vn'),
('ST23A015','Hà Văn Khoa','st23a015@uni.edu.vn'),('ST23A016','Nguyễn Thị Hương','st23a016@uni.edu.vn'),
('ST23A017','Trần Văn Lâm','st23a017@uni.edu.vn'),('ST23A018','Lê Thị Lan','st23a018@uni.edu.vn'),
('ST23A019','Phạm Văn Long','st23a019@uni.edu.vn'),('ST23A020','Hoàng Thị Linh','st23a020@uni.edu.vn'),
('ST23A021','Vũ Văn Minh','st23a021@uni.edu.vn'),('ST23A022','Đỗ Thị Mai','st23a022@uni.edu.vn'),
('ST23A023','Bùi Văn Nam','st23a023@uni.edu.vn'),('ST23A024','Hồ Thị Ngọc','st23a024@uni.edu.vn'),
('ST23A025','Đặng Văn Nhân','st23a025@uni.edu.vn'),('ST23A026','Ngô Thị Như','st23a026@uni.edu.vn'),
('ST23A027','Dương Văn Phúc','st23a027@uni.edu.vn'),('ST23A028','Lý Thị Phương','st23a028@uni.edu.vn'),
('ST23A029','Phan Văn Quân','st23a029@uni.edu.vn'),('ST23A030','Hà Thị Thảo','st23a030@uni.edu.vn')
) AS t(code, name, email)
WHERE NOT EXISTS (SELECT 1 FROM students WHERE student_code = t.code);

-- LỚP LM23A
INSERT INTO students (id, student_code, full_name, email, class_name, is_active)
SELECT gen_random_uuid(), t.code, t.name, t.email, 'LM23A', TRUE
FROM (VALUES
('LM23A001','Nguyễn Văn Sơn','lm23a001@uni.edu.vn'),('LM23A002','Trần Thị Trang','lm23a002@uni.edu.vn'),
('LM23A003','Lê Văn Thắng','lm23a003@uni.edu.vn'),('LM23A004','Phạm Thị Thư','lm23a004@uni.edu.vn'),
('LM23A005','Hoàng Văn Toàn','lm23a005@uni.edu.vn'),('LM23A006','Vũ Thị Uyên','lm23a006@uni.edu.vn'),
('LM23A007','Đỗ Văn Trung','lm23a007@uni.edu.vn'),('LM23A008','Bùi Thị Vân','lm23a008@uni.edu.vn'),
('LM23A009','Hồ Văn Tuấn','lm23a009@uni.edu.vn'),('LM23A010','Đặng Thị Yến','lm23a010@uni.edu.vn'),
('LM23A011','Ngô Văn Tài','lm23a011@uni.edu.vn'),('LM23A012','Dương Thị Liên','lm23a012@uni.edu.vn'),
('LM23A013','Lý Văn Việt','lm23a013@uni.edu.vn'),('LM23A014','Phan Thị Nhi','lm23a014@uni.edu.vn'),
('LM23A015','Hà Văn Dương','lm23a015@uni.edu.vn'),('LM23A016','Nguyễn Thị Hiền','lm23a016@uni.edu.vn'),
('LM23A017','Trần Văn Khải','lm23a017@uni.edu.vn'),('LM23A018','Lê Thị Loan','lm23a018@uni.edu.vn'),
('LM23A019','Phạm Văn Hậu','lm23a019@uni.edu.vn'),('LM23A020','Hoàng Thị Mỹ','lm23a020@uni.edu.vn'),
('LM23A021','Vũ Văn Điền','lm23a021@uni.edu.vn'),('LM23A022','Đỗ Thị Nga','lm23a022@uni.edu.vn'),
('LM23A023','Bùi Văn Giang','lm23a023@uni.edu.vn'),('LM23A024','Hồ Thị Kim','lm23a024@uni.edu.vn'),
('LM23A025','Đặng Văn Hưng','lm23a025@uni.edu.vn')
) AS t(code, name, email)
WHERE NOT EXISTS (SELECT 1 FROM students WHERE student_code = t.code);

-- LỚP EL23T
INSERT INTO students (id, student_code, full_name, email, class_name, is_active)
SELECT gen_random_uuid(), t.code, t.name, t.email, 'EL23T', TRUE
FROM (VALUES
('EL23T001','Ngô Văn Bảo','el23t001@uni.edu.vn'),('EL23T002','Dương Thị Bình','el23t002@uni.edu.vn'),
('EL23T003','Lý Văn Chiến','el23t003@uni.edu.vn'),('EL23T004','Phan Thị Cẩm','el23t004@uni.edu.vn'),
('EL23T005','Hà Văn Công','el23t005@uni.edu.vn'),('EL23T006','Nguyễn Thị Diệu','el23t006@uni.edu.vn'),
('EL23T007','Trần Văn Đông','el23t007@uni.edu.vn'),('EL23T008','Lê Thị Đoan','el23t008@uni.edu.vn'),
('EL23T009','Phạm Văn Hào','el23t009@uni.edu.vn'),('EL23T010','Hoàng Thị Hạnh','el23t010@uni.edu.vn'),
('EL23T011','Vũ Văn Hiếu','el23t011@uni.edu.vn'),('EL23T012','Đỗ Thị Huệ','el23t012@uni.edu.vn'),
('EL23T013','Bùi Văn Khánh','el23t013@uni.edu.vn'),('EL23T014','Hồ Thị Khanh','el23t014@uni.edu.vn'),
('EL23T015','Đặng Văn Kiên','el23t015@uni.edu.vn'),('EL23T016','Ngô Thị Lê','el23t016@uni.edu.vn'),
('EL23T017','Dương Văn Lực','el23t017@uni.edu.vn'),('EL23T018','Lý Thị Lộc','el23t018@uni.edu.vn'),
('EL23T019','Phan Văn Mạnh','el23t019@uni.edu.vn'),('EL23T020','Hà Thị Minh','el23t020@uni.edu.vn'),
('EL23T021','Nguyễn Văn Nghĩa','el23t021@uni.edu.vn'),('EL23T022','Trần Thị Nhung','el23t022@uni.edu.vn'),
('EL23T023','Lê Văn Phong','el23t023@uni.edu.vn'),('EL23T024','Phạm Thị Quỳnh','el23t024@uni.edu.vn'),
('EL23T025','Hoàng Văn Quý','el23t025@uni.edu.vn'),('EL23T026','Vũ Thị Sương','el23t026@uni.edu.vn'),
('EL23T027','Đỗ Văn Tâm','el23t027@uni.edu.vn'),('EL23T028','Bùi Thị Thúy','el23t028@uni.edu.vn'),
('EL23T029','Hồ Văn Thiện','el23t029@uni.edu.vn'),('EL23T030','Đặng Thị Thu','el23t030@uni.edu.vn'),
('EL23T031','Ngô Văn Thuận','el23t031@uni.edu.vn'),('EL23T032','Dương Thị Tuyền','el23t032@uni.edu.vn'),
('EL23T033','Lý Văn Vinh','el23t033@uni.edu.vn'),('EL23T034','Phan Thị Xuân','el23t034@uni.edu.vn'),
('EL23T035','Hà Văn Định','el23t035@uni.edu.vn')
) AS t(code, name, email)
WHERE NOT EXISTS (SELECT 1 FROM students WHERE student_code = t.code);

-- LỚP AI23D
INSERT INTO students (id, student_code, full_name, email, class_name, is_active)
SELECT gen_random_uuid(), t.code, t.name, t.email, 'AI23D', TRUE
FROM (VALUES
('AI23D001','Nguyễn Văn Anh','ai23d001@uni.edu.vn'),('AI23D002','Trần Thị Châu','ai23d002@uni.edu.vn'),
('AI23D003','Lê Văn Chính','ai23d003@uni.edu.vn'),('AI23D004','Phạm Thị Dung','ai23d004@uni.edu.vn'),
('AI23D005','Hoàng Văn Đức','ai23d005@uni.edu.vn'),('AI23D006','Vũ Thị Thủy','ai23d006@uni.edu.vn'),
('AI23D007','Đỗ Văn Hưng','ai23d007@uni.edu.vn'),('AI23D008','Bùi Thị Huyền','ai23d008@uni.edu.vn'),
('AI23D009','Hồ Văn Khang','ai23d009@uni.edu.vn'),('AI23D010','Đặng Thị Khánh','ai23d010@uni.edu.vn'),
('AI23D011','Ngô Văn Lợi','ai23d011@uni.edu.vn'),('AI23D012','Dương Thị Luyến','ai23d012@uni.edu.vn'),
('AI23D013','Lý Văn Ninh','ai23d013@uni.edu.vn'),('AI23D014','Phan Thị Oanh','ai23d014@uni.edu.vn'),
('AI23D015','Hà Văn Phát','ai23d015@uni.edu.vn'),('AI23D016','Nguyễn Thị Quyên','ai23d016@uni.edu.vn'),
('AI23D017','Trần Văn Sang','ai23d017@uni.edu.vn'),('AI23D018','Lê Thị Tâm','ai23d018@uni.edu.vn'),
('AI23D019','Phạm Văn Thịnh','ai23d019@uni.edu.vn'),('AI23D020','Hoàng Thị Trinh','ai23d020@uni.edu.vn')
) AS t(code, name, email)
WHERE NOT EXISTS (SELECT 1 FROM students WHERE student_code = t.code);

-- LỚP IT23A
INSERT INTO students (id, student_code, full_name, email, class_name, is_active)
SELECT gen_random_uuid(), t.code, t.name, t.email, 'IT23A', TRUE
FROM (VALUES
('IT23A001','Vũ Văn Bắc','it23a001@uni.edu.vn'),('IT23A002','Đỗ Thị Cam','it23a002@uni.edu.vn'),
('IT23A003','Bùi Văn Cao','it23a003@uni.edu.vn'),('IT23A004','Hồ Thị Cát','it23a004@uni.edu.vn'),
('IT23A005','Đặng Văn Chiến','it23a005@uni.edu.vn'),('IT23A006','Ngô Thị Chinh','it23a006@uni.edu.vn'),
('IT23A007','Dương Văn Công','it23a007@uni.edu.vn'),('IT23A008','Lý Thị Dần','it23a008@uni.edu.vn'),
('IT23A009','Phan Văn Duy','it23a009@uni.edu.vn'),('IT23A010','Hà Thị Đào','it23a010@uni.edu.vn'),
('IT23A011','Nguyễn Văn Giáp','it23a011@uni.edu.vn'),('IT23A012','Trần Thị Giỏi','it23a012@uni.edu.vn'),
('IT23A013','Lê Văn Hà','it23a013@uni.edu.vn'),('IT23A014','Phạm Thị Hải','it23a014@uni.edu.vn'),
('IT23A015','Hoàng Văn Hán','it23a015@uni.edu.vn'),('IT23A016','Vũ Thị Hiền','it23a016@uni.edu.vn'),
('IT23A017','Đỗ Văn Hoàn','it23a017@uni.edu.vn'),('IT23A018','Bùi Thị Huệ','it23a018@uni.edu.vn'),
('IT23A019','Hồ Văn Huy','it23a019@uni.edu.vn'),('IT23A020','Đặng Thị Huyền','it23a020@uni.edu.vn'),
('IT23A021','Ngô Văn Hướng','it23a021@uni.edu.vn'),('IT23A022','Dương Thị Khanh','it23a022@uni.edu.vn'),
('IT23A023','Lý Văn Lân','it23a023@uni.edu.vn'),('IT23A024','Phan Thị Lê','it23a024@uni.edu.vn'),
('IT23A025','Hà Văn Lương','it23a025@uni.edu.vn'),('IT23A026','Nguyễn Thị Mến','it23a026@uni.edu.vn'),
('IT23A027','Trần Văn Mạnh','it23a027@uni.edu.vn'),('IT23A028','Lê Thị Nụ','it23a028@uni.edu.vn'),
('IT23A029','Phạm Văn Nghiêm','it23a029@uni.edu.vn'),('IT23A030','Hoàng Thị Nho','it23a030@uni.edu.vn'),
('IT23A031','Vũ Văn Niệm','it23a031@uni.edu.vn'),('IT23A032','Đỗ Thị Nương','it23a032@uni.edu.vn'),
('IT23A033','Bùi Văn Phan','it23a033@uni.edu.vn'),('IT23A034','Hồ Thị Quyên','it23a034@uni.edu.vn'),
('IT23A035','Đặng Văn Quốc','it23a035@uni.edu.vn'),('IT23A036','Ngô Thị Phúc','it23a036@uni.edu.vn'),
('IT23A037','Dương Văn Sáng','it23a037@uni.edu.vn'),('IT23A038','Lý Thị Thu','it23a038@uni.edu.vn'),
('IT23A039','Phan Văn Sĩ','it23a039@uni.edu.vn'),('IT23A040','Hà Thị Tố','it23a040@uni.edu.vn')
) AS t(code, name, email)
WHERE NOT EXISTS (SELECT 1 FROM students WHERE student_code = t.code);

-- TẠO TÀI KHOẢN cho sinh viên lớp mới
INSERT INTO accounts (username, password, role, student_id, full_name, is_active)
SELECT s.student_code, LOWER(s.student_code), 'student', s.id, s.full_name, TRUE
FROM students s
WHERE s.class_name IN ('ST23A', 'LM23A', 'EL23T', 'AI23D', 'IT23A')
  AND NOT EXISTS (SELECT 1 FROM accounts a WHERE a.username = s.student_code);
