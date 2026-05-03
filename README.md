# 🎓 NHÓM 6: ĐĂNG KÝ HỌC LẠI – ĐĂNG KÝ HỌC PHẦN

> Stack: **Spring Boot (Java)** + **SQL Server** + **Vanilla JS/HTML/CSS** + **Postman**

---

## 📂 NỘI DUNG PROJECT

| Thư mục | Mô tả |
|---|---|
| `backend/` | Code Spring Boot (chạy được) |
| `frontend/` | Code HTML/CSS/JS (chạy được) |
| `database/` | 2 file SQL để chạy trong SSMS |
| `postman/` | Collection để import vào Postman |

---

## 📖 3 FILE HƯỚNG DẪN — ĐỌC THEO THỨ TỰ

### 1️⃣ Tạo data trong SQL Server
👉 Mở file **`HUONG_DAN_DATABASE_SSMS.md`**
- Cách kết nối SSMS
- Cách chạy 2 file `01_create_database.sql` và `02_seed_data.sql`
- Các query kiểm tra dữ liệu
- Xử lý lỗi thường gặp

### 2️⃣ Chạy code (Backend + Frontend)
👉 Mở file **`HUONG_DAN_CHAY_CODE.md`**
- Cách chạy Spring Boot bằng IntelliJ / VS Code / Terminal
- Cách mở Frontend
- Demo flow hoạt động

### 3️⃣ Test API trong Postman
👉 Mở file **`HUONG_DAN_POSTMAN.md`**
- Cách import collection
- Cách set biến môi trường
- 6 test case theo thứ tự
- Cách test các tình huống lỗi

---

## ⚡ QUICK START (3 bước)

```
[1] SSMS → chạy 01_create_database.sql + 02_seed_data.sql
[2] Terminal → cd backend && mvn spring-boot:run
[3] Trình duyệt → mở frontend/index.html
```

---

## 🎯 CHỨC NĂNG CHÍNH

### Nhóm chức năng 1 — Hiển thị môn học lại
- Tự động tính điểm tổng kết: `Σ(score × weight / 100)`
- Lọc môn có điểm `< 5.0` → môn cần học lại
- Hiển thị kèm chi tiết điểm thành phần và lớp đang mở

### Nhóm chức năng 2 — Đăng ký học lại
- Sinh viên chọn lớp đang mở → đăng ký
- Validate: đợt đang mở, lớp còn slot, không đăng ký trùng
- Tự động cập nhật số sinh viên trong lớp

### Nhóm chức năng 3 — Quản lý đăng ký
- Xem lịch sử đăng ký
- Hủy đăng ký (giảm số sinh viên trong lớp)

---

## 🧪 DỮ LIỆU TEST SẴN

| Sinh viên | Mã | Kết quả |
|---|---|---|
| Nguyễn Văn An | SV2024001 | 1 môn cần học lại (IT101: 4.40) |
| **Trần Thị Bình** | **SV2024002** | **2 môn cần học lại (IT101: 2.90, IT102: 4.90)** ← TEST CHÍNH |
| Lê Hoàng Cường | SV2024003 | Chưa có lịch sử |

---

## 🔌 API ENDPOINTS

| # | Method | Endpoint | Chức năng |
|---|---|---|---|
| 1 | GET | `/api/v1/students` | Danh sách sinh viên |
| 2 | GET | `/api/v1/registration-periods/open` | Đợt đăng ký đang mở |
| 3 | **GET** | **`/api/v1/retake/students/{id}/courses`** | **⭐ Môn cần học lại** |
| 4 | POST | `/api/v1/registrations` | Đăng ký học phần |
| 5 | DELETE | `/api/v1/registrations/{id}` | Hủy đăng ký |
| 6 | GET | `/api/v1/registrations/students/{id}` | Lịch sử đăng ký |

---

## 🗄️ BẢNG DỮ LIỆU

### Bảng tự xây (Nhóm 6)
- `registration_periods` — Đợt đăng ký
- `course_registrations` — Chi tiết đăng ký
- `equivalent_courses` — Môn tương đương

### Bảng dùng từ nhóm khác
- `students`, `courses`, `semesters` — Cơ bản
- `course_sections`, `student_course_sections` — Từ Nhóm 5
- `grade_components` — Từ Nhóm 8

---

## 🆘 KHI GẶP VẤN ĐỀ

1. **Database lỗi** → đọc `HUONG_DAN_DATABASE_SSMS.md` mục "Xử lý lỗi"
2. **Code không chạy** → đọc `HUONG_DAN_CHAY_CODE.md` mục "Lỗi thường gặp"
3. **API không gọi được** → đọc `HUONG_DAN_POSTMAN.md` mục "Xử lý lỗi"

---

> Project được làm theo cấu trúc đề bài: 1. Mô tả chức năng → 2. Cấu trúc bảng → 3. API → 4. Giao diện → 5. Kết quả.
