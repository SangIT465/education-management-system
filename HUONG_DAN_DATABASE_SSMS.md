# 📘 HƯỚNG DẪN TẠO DATABASE TRÊN SQL SERVER MANAGEMENT STUDIO (SSMS)

> Hướng dẫn từng bước cho người mới. Làm theo đúng thứ tự!

---

## 🔧 BƯỚC 0: CHUẨN BỊ

### Cài đặt cần thiết
1. **SQL Server** (bản Express miễn phí): https://www.microsoft.com/en-us/sql-server/sql-server-downloads
2. **SQL Server Management Studio (SSMS)**: https://learn.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms

### Khi cài SQL Server, hãy chú ý:
- Chọn chế độ **Mixed Mode Authentication** (cho phép cả Windows + SQL Server login)
- Đặt password cho user `sa` → **GHI NHỚ password này** (ví dụ: `YourStrong@Passw0rd`)
- Bật **TCP/IP** trong SQL Server Configuration Manager

---

## 🚀 BƯỚC 1: KẾT NỐI SSMS VÀO SQL SERVER

1. Mở **SQL Server Management Studio**
2. Cửa sổ **Connect to Server** hiện ra:
   - **Server type**: `Database Engine`
   - **Server name**: `localhost` (hoặc `.\SQLEXPRESS` nếu cài bản Express)
   - **Authentication**: `SQL Server Authentication`
   - **Login**: `sa`
   - **Password**: password bạn đã đặt
3. Click **Connect**

✅ **Kết quả**: Bên trái có cây thư mục **Object Explorer** hiện ra.

---

## 🗄️ BƯỚC 2: TẠO DATABASE

### Cách 1: Dùng giao diện
1. Click chuột phải vào **Databases** → chọn **New Database...**
2. **Database name**: `university_retake_db`
3. Click **OK**

### Cách 2: Dùng query (khuyến nghị)
1. Click nút **New Query** (góc trên trái) hoặc nhấn `Ctrl + N`
2. Dán đoạn SQL sau:
```sql
CREATE DATABASE university_retake_db;
GO
USE university_retake_db;
GO
```
3. Nhấn **F5** để chạy (hoặc click nút **Execute**)

✅ **Kết quả**: Cây Databases có thêm `university_retake_db`. Click chuột phải → **Refresh** nếu chưa thấy.

---

## 📋 BƯỚC 3: TẠO CÁC BẢNG (RUN SCRIPT 01)

1. Trong **Object Explorer**, click chuột phải vào `university_retake_db` → chọn **New Query**
2. Mở file `database/01_create_database.sql` (đã cung cấp)
3. **Copy toàn bộ nội dung** dán vào cửa sổ query
4. **QUAN TRỌNG**: Đảm bảo dropdown trên cùng đang chọn database `university_retake_db` (không phải `master`)

   ![Database selector ở góc trên trái thanh công cụ]

5. Nhấn **F5** để chạy

✅ **Kết quả mong đợi** (xem ở khung Messages dưới cùng):
```
Tạo database và bảng thành công!

Commands completed successfully.
```

### Kiểm tra các bảng đã tạo
Mở cây: `Databases` → `university_retake_db` → `Tables`. Bạn phải thấy đủ **9 bảng**:
- `courses`
- `students`
- `semesters`
- `course_sections`
- `student_course_sections`
- `grade_components`
- `registration_periods`
- `course_registrations`
- `equivalent_courses`

---

## 🌱 BƯỚC 4: CHÈN DỮ LIỆU MẪU (RUN SCRIPT 02)

1. **New Query** trên database `university_retake_db`
2. Mở file `database/02_seed_data.sql` → copy toàn bộ → dán vào
3. Nhấn **F5**

✅ **Kết quả mong đợi**:
```
Đã chèn dữ liệu mẫu thành công!
==> Sinh viên SV2024001 (An) trượt môn IT101 -> cần học lại
==> Sinh viên SV2024002 (Bình) trượt môn IT101 và IT102 -> cần học lại
```

---

## 🔍 BƯỚC 5: KIỂM TRA DỮ LIỆU

Chạy các query sau để xem dữ liệu vừa tạo:

### 5.1. Xem danh sách sinh viên
```sql
USE university_retake_db;
SELECT id, student_code, full_name, class_name FROM students;
```

➡️ Phải thấy 3 sinh viên: An, Bình, Cường.

**📌 GHI LẠI giá trị cột `id` của sinh viên Bình** — bạn sẽ cần nó cho Postman!

### 5.2. Xem lịch sử học của sinh viên
```sql
SELECT
    s.student_code,
    s.full_name,
    c.code AS course_code,
    c.name AS course_name,
    scs.status
FROM student_course_sections scs
JOIN students s ON s.id = scs.student_id
JOIN course_sections cs ON cs.id = scs.course_section_id
JOIN courses c ON c.id = cs.course_id
ORDER BY s.student_code, c.code;
```

### 5.3. Xem điểm thành phần của các môn
```sql
SELECT
    s.student_code,
    s.full_name,
    c.code AS course_code,
    gc.component_code,
    gc.weight_percentage,
    gc.score
FROM grade_components gc
JOIN student_course_sections scs ON scs.id = gc.student_course_section_id
JOIN students s ON s.id = scs.student_id
JOIN course_sections cs ON cs.id = scs.course_section_id
JOIN courses c ON c.id = cs.course_id
ORDER BY s.student_code, c.code, gc.component_code;
```

### 5.4. Tính điểm tổng kết của Bình (kiểm tra logic)
```sql
SELECT
    s.full_name,
    c.code AS course_code,
    SUM(gc.score * gc.weight_percentage / 100) AS total_score,
    CASE
        WHEN SUM(gc.score * gc.weight_percentage / 100) < 5.0 THEN N'TRƯỢT'
        ELSE N'ĐẬU'
    END AS ket_qua
FROM grade_components gc
JOIN student_course_sections scs ON scs.id = gc.student_course_section_id
JOIN students s ON s.id = scs.student_id
JOIN course_sections cs ON cs.id = scs.course_section_id
JOIN courses c ON c.id = cs.course_id
WHERE s.student_code = 'SV2024002'
GROUP BY s.full_name, c.code;
```

✅ **Kết quả mong đợi**:
| full_name | course_code | total_score | ket_qua |
|---|---|---|---|
| Trần Thị Bình | IT101 | 2.90 | TRƯỢT |
| Trần Thị Bình | IT102 | 4.90 | TRƯỢT |

### 5.5. Xem các lớp học phần đang mở để đăng ký
```sql
SELECT code, name, status, current_students, max_students, class_type
FROM course_sections
WHERE status = 'open' AND is_active = 1;
```

### 5.6. Xem đợt đăng ký đang mở
```sql
SELECT id, name, start_time, end_time, is_open
FROM registration_periods
WHERE is_open = 1;
```

**📌 GHI LẠI giá trị `id` của đợt đăng ký** — bạn sẽ cần nó cho Postman!

---

## 🛠️ BƯỚC 6: CẤU HÌNH KẾT NỐI VỚI SPRING BOOT

Mở file `backend/src/main/resources/application.properties` và **chỉnh** 3 dòng sau cho khớp với máy bạn:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=university_retake_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourStrong@Passw0rd
```

### Các trường hợp cần đổi:
| Trường hợp | Sửa thành |
|---|---|
| Dùng SQL Server Express | `jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=university_retake_db;...` |
| Dùng port khác 1433 | `jdbc:sqlserver://localhost:PORT_KHÁC;...` |
| Lỗi SSL | giữ nguyên `encrypt=true;trustServerCertificate=true` |
| Password khác | sửa `spring.datasource.password=` |

---

## 🚦 BƯỚC 7: BẬT TCP/IP (NẾU CHƯA BẬT)

Nếu Spring Boot báo lỗi không kết nối được, làm theo:

1. Mở **SQL Server Configuration Manager**
2. Vào: `SQL Server Network Configuration` → `Protocols for MSSQLSERVER` (hoặc SQLEXPRESS)
3. Chuột phải **TCP/IP** → **Enable**
4. Vào tab `IP Addresses` → kéo xuống `IPAll`:
   - **TCP Port**: `1433`
   - **TCP Dynamic Ports**: để trống
5. Restart **SQL Server (MSSQLSERVER)** trong cửa sổ Services

---

## ❌ XỬ LÝ LỖI THƯỜNG GẶP

### Lỗi 1: "Login failed for user 'sa'"
- Vào SSMS → chuột phải tên server → **Properties** → **Security** → chọn **SQL Server and Windows Authentication mode**
- Restart SQL Server service
- Vào `Security` → `Logins` → chuột phải `sa` → **Properties**:
  - Tab **General**: nhập password mới
  - Tab **Status**: chọn `Enabled` cho Login

### Lỗi 2: "Cannot open database 'university_retake_db'"
- Bạn đang ở database `master`. Chạy `USE university_retake_db;` trước query khác.

### Lỗi 3: "There is already an object named 'courses'"
- Database đã có sẵn bảng. Chạy lại `01_create_database.sql` — script đã có lệnh DROP TABLE để xóa cũ.

### Lỗi 4: Spring Boot báo "Connection refused"
- Kiểm tra TCP/IP ở **Bước 7**
- Kiểm tra Windows Firewall không chặn port 1433
- Thử ping: trong CMD chạy `telnet localhost 1433` (cần bật Telnet Client)

---

## 📊 SƠ ĐỒ QUAN HỆ CÁC BẢNG

```
students ───┬──── student_course_sections ──── grade_components
            │              │
            │              └─── course_sections ──── courses
            │                          │
            │                          └─── semesters
            │
            └──── course_registrations ──── registration_periods
                          │
                          └─── course_sections
```

---

## ✅ CHECKLIST TRƯỚC KHI CHẠY BACKEND

- [ ] Đã tạo database `university_retake_db` thành công
- [ ] Đã chạy script `01_create_database.sql` — có 9 bảng
- [ ] Đã chạy script `02_seed_data.sql` — có sinh viên & dữ liệu mẫu
- [ ] Query test ở Bước 5 trả về dữ liệu đúng
- [ ] Đã sửa `application.properties` cho khớp với máy
- [ ] Đã bật TCP/IP cho SQL Server
- [ ] Đã ghi lại `id` của sinh viên Bình & đợt đăng ký để dùng cho Postman

Sau khi xong tất cả → chuyển sang chạy Backend Spring Boot:
```bash
cd backend
mvn spring-boot:run
```
