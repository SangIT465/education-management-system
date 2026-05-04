# Hướng dẫn tạo dữ liệu trong SSMS

## 1. Kết nối SQL Server

Mở **SQL Server Management Studio (SSMS)**, điền thông tin:

| Trường | Giá trị |
|---|---|
| Server type | Database Engine |
| Server name | `localhost` |
| Authentication | SQL Server Authentication |
| Login | `sa` |
| Password | `123` |

Bấm **Connect**.

---

## 2. Chạy 3 file SQL (chỉ làm 1 lần)

Trong SSMS, làm theo thứ tự:

### Bước 1 — Tạo bảng
```
File → Open → File...
Chọn: database/01_create_database.sql
Nhấn F5 (hoặc bấm Execute)
```
Kết quả: xuất hiện database `university_retake_db` trong cây bên trái.

### Bước 2 — Nhập dữ liệu mẫu
```
File → Open → File...
Chọn: database/02_seed_data.sql
Nhấn F5
```
Kết quả: có sẵn sinh viên, môn học, điểm, lớp học phần mẫu.

### Bước 3 — Tạo bảng tài khoản
```
File → Open → File...
Chọn: database/03_add_auth.sql
Nhấn F5
```
Kết quả: có bảng `accounts` với 4 tài khoản mặc định.

---

## 3. Kiểm tra dữ liệu đã có

Mở cửa sổ **New Query** (Ctrl+N), chọn database `university_retake_db` ở dropdown trên, rồi chạy:

```sql
USE university_retake_db;

-- Xem tài khoản
SELECT username, role, full_name, is_active FROM accounts;

-- Xem sinh viên
SELECT student_code, full_name, class_name FROM students WHERE is_active = 1;

-- Xem môn học
SELECT code, name, credits FROM courses WHERE is_active = 1;

-- Xem lớp học phần
SELECT cs.code, c.name, cs.status, cs.current_students, cs.max_students
FROM course_sections cs
JOIN courses c ON cs.course_id = c.id
WHERE cs.is_active = 1;

-- Xem đợt đăng ký
SELECT name, is_open, start_time, end_time FROM registration_periods WHERE is_active = 1;

-- Xem lịch sử đăng ký
SELECT cr.id, s.student_code, cs.code as section_code, cr.registration_type, cr.status
FROM course_registrations cr
JOIN students s ON cr.student_id = s.id
JOIN course_sections cs ON cr.course_section_id = cs.id;
```

---

## 4. Thêm dữ liệu thủ công qua SSMS

> Thông thường nên thêm qua **giao diện Admin** trên web. Dưới đây là cách thêm thẳng vào SQL nếu cần.

### Thêm sinh viên
```sql
USE university_retake_db;

INSERT INTO students (id, student_code, full_name, email, class_name, is_active)
VALUES (NEWID(), 'SV2024010', N'Phạm Văn E', 'e.pv@uni.edu.vn', N'CNTT-K18', 1);
```

### Thêm môn học
```sql
INSERT INTO courses (id, code, name, credits, is_active)
VALUES (NEWID(), 'IT201', N'Lập trình Web', 3, 1);
```

### Thêm đợt đăng ký mới (mở ngay)
```sql
INSERT INTO registration_periods (id, name, semester_id, start_time, end_time, min_credits, max_credits, allow_retake, is_open, is_active, created_at, updated_at)
SELECT
  NEWID(),
  N'Đợt đăng ký HK2 2025-2026',
  id,           -- lấy id học kỳ đầu tiên
  GETDATE(),
  DATEADD(DAY, 14, GETDATE()),
  12, 25, 1, 1, 1,
  GETDATE(), GETDATE()
FROM semesters WHERE is_active = 1;
```

### Thêm tài khoản sinh viên mới
```sql
INSERT INTO accounts (id, username, password, role, student_id, full_name, is_active, created_at)
SELECT
  NEWID(),
  'SV2024010',
  'sv010',
  'student',
  id,
  full_name,
  1,
  GETDATE()
FROM students WHERE student_code = 'SV2024010';
```

---

## 5. Xem dữ liệu mới sau khi thêm qua web

Sau mỗi lần thêm/sửa/xóa trên giao diện Admin:

1. Quay lại SSMS
2. Nhấn **F5** để chạy lại query
3. Dữ liệu mới xuất hiện ngay

> SSMS không tự refresh — phải nhấn F5 thủ công.

---

## 6. Reset dữ liệu về ban đầu

Nếu muốn xóa hết và bắt đầu lại:

```sql
USE master;
DROP DATABASE university_retake_db;
```

Sau đó chạy lại 3 file SQL từ Bước 2.

---

## Lỗi thường gặp

| Lỗi | Nguyên nhân | Cách sửa |
|---|---|---|
| Cannot connect to localhost | SQL Server chưa chạy | Mở Services.msc → khởi động `SQL Server (MSSQLSERVER)` |
| Login failed for user 'sa' | Sai mật khẩu hoặc sa bị tắt | Dùng Windows Authentication rồi bật sa |
| Invalid object name 'accounts' | Chưa chạy `03_add_auth.sql` | Chạy lại file đó |
| Database already exists | Đã tạo rồi | Bỏ qua lỗi hoặc xóa database cũ trước |
