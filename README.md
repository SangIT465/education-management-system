# Nhóm 6 — Hệ thống Đăng ký Học lại

Ứng dụng web quản lý việc đăng ký học lại cho sinh viên. Admin nhập điểm và phân loại tự động; sinh viên xem môn cần học lại và đăng ký lớp học phần.

Stack: **Spring Boot 3.2 (Java 17)** · **SQL Server** · **Vanilla JS / HTML / CSS**

---

## Cấu trúc thư mục

```
group6_retake_registration/
├── backend/                        # Spring Boot REST API (port 8080)
│   ├── src/main/java/...
│   └── src/main/resources/
│       └── application.properties  # Cấu hình DB, port
├── frontend/                       # Giao diện tĩnh HTML/CSS/JS
│   ├── login.html                  # Đăng nhập / đăng ký tài khoản
│   ├── index.html                  # Trang sinh viên
│   ├── admin.html                  # Trang quản trị
│   ├── css/
│   │   ├── styles.css              # Design system chung
│   │   ├── admin.css               # Style trang admin
│   │   └── auth.css                # Style trang đăng nhập
│   └── js/
│       ├── app.js                  # Logic trang sinh viên
│       └── admin.js                # Logic trang admin
├── database/                       # Script SQL (chạy một lần)
│   ├── 01_create_database.sql      # Tạo database và các bảng
│   ├── 02_seed_data.sql            # Dữ liệu mẫu
│   └── 03_add_auth.sql             # Bảng tài khoản đăng nhập
├── postman/                        # Collection test API
├── HUONG_DAN_SSMS.md               # Hướng dẫn thao tác SSMS
└── HUONG_DAN_POSTMAN.md            # Hướng dẫn test API bằng Postman
```

---

## Yêu cầu cài đặt

| Phần mềm | Phiên bản | Ghi chú |
|---|---|---|
| Java JDK | 17 trở lên | Cần set biến môi trường `JAVA_HOME` |
| Apache Maven | 3.6 trở lên | Cần có lệnh `mvn` trong PATH |
| SQL Server | 2019 trở lên | |
| SQL Server Management Studio (SSMS) | Bất kỳ | Để chạy script SQL |
| Trình duyệt | Chrome / Edge / Firefox | |

---

## Các bước chạy dự án

### Bước 1 — Khởi tạo database (chỉ làm một lần)

Mở **SSMS**, kết nối với:
- Server: `localhost`
- Authentication: SQL Server Authentication
- Login: `sa` · Password: `123`

Chạy lần lượt 3 file SQL sau (**File → Open → F5** để chạy từng file):

```
database/01_create_database.sql   ← tạo database và các bảng
database/02_seed_data.sql         ← nhập dữ liệu mẫu (sinh viên, môn học, điểm...)
database/03_add_auth.sql          ← tạo tài khoản đăng nhập
```

Kiểm tra thành công:
```sql
USE university_retake_db;
SELECT * FROM accounts;
-- Phải thấy 4 dòng: admin + SV2024001 + SV2024002 + SV2024003
```

> Sau lần đầu, các lần chạy sau **không cần làm lại** bước này.

---

### Bước 2 — Chạy Backend

Mở **PowerShell** hoặc **CMD** mới, chạy:

```powershell
cd D:\group6_retake_registration\backend
mvn spring-boot:run
```

Đợi đến khi thấy dòng sau là backend đã sẵn sàng:
```
Started RetakeRegistrationApplication in X.X seconds
========================================
  GROUP 6 - RETAKE REGISTRATION API
  Server: http://localhost:8080/api
========================================
```

**Giữ terminal này mở** trong suốt quá trình sử dụng. Để dừng backend: nhấn **Ctrl + C**.

> **Lỗi `Port 8080 already in use`** — process cũ đang chiếm cổng. Chạy lệnh sau để kill, rồi thử lại:
> ```powershell
> Get-NetTCPConnection -LocalPort 8080 | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }
> ```

---

### Bước 3 — Mở Frontend

Mở file `frontend/login.html` bằng trình duyệt:

- **Cách 1 — Đơn giản nhất:** Double-click vào file `frontend/login.html`
- **Cách 2 — VS Code:** Cài extension **Live Server** → chuột phải `login.html` → *Open with Live Server*

Đăng nhập bằng tài khoản demo (xem bảng bên dưới).

---

## Tài khoản demo

| Tài khoản | Mật khẩu | Vai trò |
|---|---|---|
| `admin` | `admin123` | Quản trị viên — vào `admin.html` |
| `SV2024001` | `sv001` | Nguyễn Văn An |
| `SV2024002` | `sv002` | Trần Thị Bình |
| `SV2024003` | `sv003` | Lê Hoàng Cường |

---

## Cấu hình kết nối database

File cấu hình: `backend/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=university_retake_db;...
spring.datasource.username=sa
spring.datasource.password=123
```

Nếu SQL Server của bạn dùng **tên server khác** hoặc **mật khẩu khác**, sửa 3 dòng trên rồi restart backend.

---

## Chức năng

### Sinh viên (`index.html`)
- Xem danh sách môn cần học lại (điểm tổng kết < 5.0)
- Xem chi tiết điểm thành phần (QT / GK / CK) từng môn
- Đăng ký lớp học phần đang mở trong đợt đăng ký hiện tại
- Xem lịch sử đăng ký và hủy đăng ký
- Thông tin cá nhân hiển thị trên header, có thể đăng xuất

### Admin (`admin.html`)
- **Sinh viên:** Thêm / sửa / xóa sinh viên
- **Môn học:** Thêm / sửa / xóa môn học
- **Học kỳ:** Quản lý các học kỳ
- **Lớp học phần:** Tạo lớp, thiết lập sĩ số, trạng thái mở/đóng
- **Điểm số:** Nhập điểm thành phần (QT / GK / CK) cho từng sinh viên — tự động tính tổng và phân loại:
  - Tổng kết **< 5.0** → Cần học lại (đỏ)
  - Tổng kết **5.0 – 6.9** → Cải thiện được (vàng)
  - Tổng kết **≥ 7.0** → Đạt (xanh)
- **Đợt đăng ký:** Tạo và quản lý các đợt đăng ký học lại

---

## API

### Auth & Sinh viên

| Method | Endpoint | Chức năng |
|---|---|---|
| POST | `/api/v1/auth/login` | Đăng nhập |
| POST | `/api/v1/auth/register` | Đăng ký tài khoản sinh viên |
| GET | `/api/v1/students` | Danh sách sinh viên đang hoạt động |
| GET | `/api/v1/registration-periods/open` | Đợt đăng ký đang mở |
| GET | `/api/v1/retake/students/{id}/courses` | Môn cần học lại của sinh viên |
| GET | `/api/v1/course-sections/open` | Lớp học phần đang mở |
| POST | `/api/v1/registrations` | Đăng ký học phần |
| DELETE | `/api/v1/registrations/{id}` | Hủy đăng ký |
| GET | `/api/v1/registrations/students/{id}` | Lịch sử đăng ký của sinh viên |

### Admin

| Method | Endpoint | Chức năng |
|---|---|---|
| GET/POST | `/api/v1/admin/students` | Danh sách / thêm sinh viên |
| PUT/DELETE | `/api/v1/admin/students/{id}` | Sửa / xóa sinh viên |
| GET/POST | `/api/v1/admin/courses` | Danh sách / thêm môn học |
| PUT/DELETE | `/api/v1/admin/courses/{id}` | Sửa / xóa môn học |
| GET/POST | `/api/v1/admin/semesters` | Danh sách / thêm học kỳ |
| GET/POST | `/api/v1/admin/course-sections` | Danh sách / thêm lớp học phần |
| GET/POST | `/api/v1/admin/registration-periods` | Danh sách / thêm đợt đăng ký |
| GET | `/api/v1/admin/grade-components?studentId=` | Điểm thành phần của sinh viên |
| POST | `/api/v1/admin/grade-components` | Nhập / ghi đè điểm |
| PUT | `/api/v1/admin/grade-components/{id}` | Sửa một điểm thành phần |
| DELETE | `/api/v1/admin/grade-components/section/{scsId}` | Xóa toàn bộ điểm một môn |

---

## Xử lý lỗi thường gặp

| Lỗi | Nguyên nhân | Cách sửa |
|---|---|---|
| `Port 8080 already in use` | Backend cũ chưa tắt | Chạy lệnh kill port ở Bước 2 |
| `Cannot connect to SQL Server` | SQL Server chưa chạy | Mở **Services** → khởi động `SQL Server (MSSQLSERVER)` |
| `Invalid object name 'accounts'` | Chưa chạy `03_add_auth.sql` | Chạy lại file SQL đó trong SSMS |
| `Login/password không đúng` | Sai tài khoản | Xem bảng tài khoản demo |
| Không kết nối được server | Backend chưa chạy | Chạy lại Bước 2 |
| Không thấy dữ liệu mới trong SSMS | Cần refresh | Nhấn **F5** trong SSMS |
| `HikariPool - Connection is not available` | SQL Server tắt hoặc sai password | Kiểm tra `application.properties` và khởi động SQL Server |

---

> Nhóm 6 · Spring Boot 3.2 + SQL Server + Vanilla JS · Hệ thống Quản lý Đăng ký Học lại


# Kiểm tra SMSS đang chạy hay không:
Get-Service -Name "MSSQL*" | Select-Object Name, Status

# Truy vấn 
USE university_retake_db;                                             
                                                                        
  -- SINH VIÊN                                                          
  SELECT student_code, full_name, email, class_name
  FROM students                                                         
  WHERE is_active = 1;                    

  -- MÔN HỌC
  SELECT code, name, credits, description
  FROM courses
  WHERE is_active = 1;

  -- HỌC KỲ
  SELECT code, name, academic_year, start_date, end_date
  FROM semesters
  WHERE is_active = 1;

  -- LỚP HỌC PHẦN
  SELECT cs.code, c.name AS mon_hoc, s.name AS hoc_ky,
         cs.class_type, cs.status, cs.current_students, cs.max_students
  FROM course_sections cs
  JOIN courses c ON cs.course_id = c.id
  JOIN semesters s ON cs.semester_id = s.id
  WHERE cs.is_active = 1;

  -- ĐIỂM SỐ (kèm tên sinh viên và môn học)
  SELECT st.student_code, st.full_name, c.name AS mon_hoc,
         cs.code AS lop_hp, gc.component_name, gc.weight_percentage,
  gc.score
  FROM grade_components gc
  JOIN student_course_sections scs ON gc.student_course_section_id =
  scs.id
  JOIN students st ON scs.student_id = st.id
  JOIN course_sections cs ON scs.course_section_id = cs.id
  JOIN courses c ON cs.course_id = c.id
  ORDER BY st.student_code, c.name;

  -- ĐỢT ĐĂNG KÝ
  SELECT rp.name, s.name AS hoc_ky, rp.start_time, rp.end_time,
         rp.min_credits, rp.max_credits, rp.is_open
  FROM registration_periods rp
  JOIN semesters s ON rp.semester_id = s.id
  WHERE rp.is_active = 1; 