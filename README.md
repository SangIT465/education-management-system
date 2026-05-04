# Nhóm 6 — Hệ thống Đăng ký Học lại

Stack: **Spring Boot 3.2 (Java 17)** · **SQL Server** · **Vanilla JS / HTML / CSS**

---

## Cấu trúc thư mục

```
group6_retake_registration/
├── backend/        # Spring Boot REST API (port 8080)
├── frontend/       # Giao diện HTML/CSS/JS
│   ├── login.html      # Trang đăng nhập / đăng ký
│   ├── index.html      # Trang sinh viên
│   ├── admin.html      # Trang quản trị
│   ├── css/
│   └── js/
├── database/       # Script SQL
│   ├── 01_create_database.sql   # Tạo bảng
│   ├── 02_seed_data.sql         # Dữ liệu mẫu
│   └── 03_add_auth.sql          # Bảng tài khoản
└── postman/        # Collection test API
```

---

## Yêu cầu

| Phần mềm | Phiên bản |
|---|---|
| Java JDK | 17 trở lên |
| SQL Server | 2019 trở lên |
| Trình duyệt | Chrome / Edge / Firefox |

---

## Các bước chạy dự án

### Bước 1 — Tạo database

Mở **SSMS**, kết nối:
- Server: `localhost`
- Login: `sa` / Password: `123`

Chạy lần lượt 3 file SQL (File → Open → chạy từng file bằng F5):

```
database/01_create_database.sql   ← tạo bảng
database/02_seed_data.sql         ← nhập dữ liệu mẫu
database/03_add_auth.sql          ← tạo tài khoản đăng nhập
```

Kiểm tra thành công:
```sql
USE university_retake_db;
SELECT * FROM accounts;
```
Phải thấy ít nhất 4 tài khoản (admin + 3 sinh viên).

---

### Bước 2 — Chạy Backend

Mở terminal, chạy:

```bash
cd backend
mvnw.cmd spring-boot:run
```

Đợi đến khi thấy dòng:
```
Started RetakeRegistrationApplication in X seconds
```

Backend chạy tại: `http://localhost:8080`

> Nếu báo lỗi port 8080 đang dùng, tắt process cũ:
> ```powershell
> $p = Get-NetTCPConnection -LocalPort 8080 -State Listen | Select-Object -First 1
> Stop-Process -Id $p.OwningProcess -Force
> ```

---

### Bước 3 — Mở Frontend

Mở file `frontend/login.html` bằng trình duyệt (double-click hoặc dùng Live Server trong VS Code).

---

## Tài khoản demo

| Tài khoản | Mật khẩu | Quyền |
|---|---|---|
| `admin` | `admin123` | Admin — vào trang quản trị |
| `SV2024001` | `sv001` | Sinh viên — Nguyễn Văn An |
| `SV2024002` | `sv002` | Sinh viên — Trần Thị Bình |
| `SV2024003` | `sv003` | Sinh viên — Lê Hoàng Cường |

---

## Chức năng

### Sinh viên (`index.html`)
- Xem danh sách môn cần học lại (điểm < 5.0)
- Xem chi tiết điểm thành phần từng môn
- Đăng ký lớp học phần đang mở
- Xem và hủy đăng ký
- Thông tin cá nhân hiển thị trên header

### Admin (`admin.html`)
- Quản lý sinh viên, môn học, học kỳ
- Quản lý lớp học phần
- Quản lý đợt đăng ký
- Chỉnh sửa điểm sinh viên

---

## API chính

| Method | Endpoint | Chức năng |
|---|---|---|
| POST | `/api/v1/auth/login` | Đăng nhập |
| POST | `/api/v1/auth/register` | Đăng ký tài khoản |
| GET | `/api/v1/students` | Danh sách sinh viên |
| GET | `/api/v1/registration-periods/open` | Đợt đăng ký đang mở |
| GET | `/api/v1/retake/students/{id}/courses` | Môn cần học lại |
| GET | `/api/v1/course-sections/open` | Lớp học phần đang mở |
| POST | `/api/v1/registrations` | Đăng ký học phần |
| DELETE | `/api/v1/registrations/{id}` | Hủy đăng ký |
| GET | `/api/v1/registrations/students/{id}` | Lịch sử đăng ký |

Admin API: `GET/POST/PUT/DELETE /api/v1/admin/{students|courses|semesters|course-sections|registration-periods}`

---

## Xử lý lỗi thường gặp

| Lỗi | Nguyên nhân | Cách sửa |
|---|---|---|
| `Port 8080 already in use` | Backend đang chạy rồi | Tắt process cũ theo hướng dẫn Bước 2 |
| `Cannot connect to SQL Server` | SQL Server chưa chạy | Mở Services → khởi động SQL Server |
| `Invalid object name 'accounts'` | Chưa chạy `03_add_auth.sql` | Chạy lại file SQL đó trong SSMS |
| `Login thất bại` | Sai tài khoản/mật khẩu | Xem bảng tài khoản demo ở trên |
| Không thấy dữ liệu mới trong SSMS | Cần refresh | Nhấn F5 trong SSMS |

---

> Nhóm 6 · Spring Boot + SQL Server + Vanilla JS
