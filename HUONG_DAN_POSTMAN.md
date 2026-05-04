# Hướng dẫn test API bằng Postman

## 1. Cài đặt và import Collection

### Bước 1 — Tải Postman
Tải tại: https://www.postman.com/downloads/ → cài và mở lên.

### Bước 2 — Import Collection
```
Bấm Import (góc trên bên trái)
→ Chọn file: postman/Group6_RetakeRegistration.postman_collection.json
→ Bấm Import
```
Collection **"Group 6 - Retake Registration API"** xuất hiện trong danh sách bên trái.

### Bước 3 — Đảm bảo backend đang chạy
Mở terminal, chạy:
```bash
cd backend
mvnw.cmd spring-boot:run
```
Đợi thấy `Started RetakeRegistrationApplication`.

---

## 2. Lấy các ID cần thiết

Trước khi test, cần lấy UUID từ database. Chạy query sau trong SSMS:

```sql
USE university_retake_db;

-- Lấy studentId
SELECT id, student_code, full_name FROM students WHERE is_active = 1;

-- Lấy periodId (đợt đang mở)
SELECT id, name FROM registration_periods WHERE is_open = 1 AND is_active = 1;

-- Lấy sectionId (lớp đang mở)
SELECT cs.id, cs.code, c.name FROM course_sections cs
JOIN courses c ON cs.course_id = c.id
WHERE cs.status = 'open' AND cs.is_active = 1;
```

---

## 3. Test từng API theo thứ tự

### Test 1 — Đăng nhập

**POST** `http://localhost:8080/api/v1/auth/login`

Tab **Body → raw → JSON:**
```json
{
  "username": "SV2024002",
  "password": "sv002"
}
```

Kết quả mong đợi:
```json
{
  "success": true,
  "message": "Đăng nhập thành công",
  "data": {
    "username": "SV2024002",
    "role": "student",
    "studentId": "...",
    "studentCode": "SV2024002",
    "fullName": "Trần Thị Bình"
  }
}
```
> Lưu lại giá trị `data.studentId` để dùng cho các bước tiếp theo.

---

### Test 2 — Lấy danh sách sinh viên

**GET** `http://localhost:8080/api/v1/students`

Không cần Body. Kết quả trả về danh sách sinh viên `is_active = true`.

---

### Test 3 — Lấy đợt đăng ký đang mở

**GET** `http://localhost:8080/api/v1/registration-periods/open`

Kết quả trả về danh sách đợt có `is_open = true`.

---

### Test 4 — Xem môn cần học lại ⭐

**GET** `http://localhost:8080/api/v1/retake/students/{studentId}/courses`

Thay `{studentId}` bằng UUID lấy ở Test 1. Ví dụ:
```
GET http://localhost:8080/api/v1/retake/students/c803d8d7-5543-43ba-96a5-d53ba7970a11/courses
```

Kết quả trả về các môn có điểm tổng kết `< 5.0`, kèm danh sách lớp đang mở.

---

### Test 5 — Đăng ký học phần

**POST** `http://localhost:8080/api/v1/registrations`

Tab **Body → raw → JSON** (thay UUID thực tế):
```json
{
  "studentId": "c803d8d7-5543-43ba-96a5-d53ba7970a11",
  "courseSectionId": "06a0bc47-252d-4419-a8de-26af71f2d55d",
  "registrationPeriodId": "82d5b202-5d92-4a70-aaeb-27cd684a6387",
  "registrationType": "RETAKE",
  "note": "Đăng ký học lại"
}
```

Kết quả mong đợi:
```json
{
  "success": true,
  "data": {
    "id": "...",
    "status": "APPROVED"
  }
}
```
> Lưu lại `data.id` (registrationId) để dùng cho Test 7.

> `registrationType` có thể là: `RETAKE` (học lại), `NEW` (học mới), `IMPROVE` (cải thiện điểm)

---

### Test 6 — Xem lịch sử đăng ký

**GET** `http://localhost:8080/api/v1/registrations/students/{studentId}`

Thay `{studentId}` bằng UUID sinh viên. Trả về tất cả đăng ký của sinh viên đó.

---

### Test 7 — Hủy đăng ký

**DELETE** `http://localhost:8080/api/v1/registrations/{registrationId}`

Thay `{registrationId}` bằng ID lấy ở Test 5. Không cần Body.

Kết quả mong đợi:
```json
{
  "success": true,
  "data": "Hủy đăng ký thành công"
}
```

---

## 4. Test API Admin

> Các API này dùng để quản lý dữ liệu, tương ứng với các thao tác trong trang Admin.

### Xem danh sách
| Method | URL |
|---|---|
| GET | `http://localhost:8080/api/v1/admin/students` |
| GET | `http://localhost:8080/api/v1/admin/courses` |
| GET | `http://localhost:8080/api/v1/admin/semesters` |
| GET | `http://localhost:8080/api/v1/admin/course-sections` |
| GET | `http://localhost:8080/api/v1/admin/registration-periods` |

### Thêm sinh viên
**POST** `http://localhost:8080/api/v1/admin/students`
```json
{
  "studentCode": "SV2024020",
  "fullName": "Nguyễn Văn Mới",
  "email": "moi@uni.edu.vn",
  "className": "CNTT-K20"
}
```

### Sửa sinh viên
**PUT** `http://localhost:8080/api/v1/admin/students/{id}`
```json
{
  "fullName": "Nguyễn Văn Mới UPDATED",
  "className": "CNTT-K21"
}
```

### Xóa sinh viên
**DELETE** `http://localhost:8080/api/v1/admin/students/{id}`

---

## 5. Test các trường hợp lỗi

### Đăng nhập sai mật khẩu
```json
{ "username": "admin", "password": "sai" }
```
Kết quả: HTTP 400 — `"Mật khẩu không đúng"`

### Đăng ký khi lớp đã đầy
Khi `remainingSlots = 0`, server trả về HTTP 400.

### Đăng ký trùng
Đăng ký cùng 1 lớp 2 lần → HTTP 400 — `"Sinh viên đã đăng ký lớp học phần này"`

### Đợt đăng ký đóng
Nếu `is_open = false` → HTTP 400 — `"Đợt đăng ký không còn mở"`

---

## 6. Sử dụng biến trong Collection

Collection đã định nghĩa sẵn các biến. Vào **Collection → Variables** để điền:

| Biến | Giá trị |
|---|---|
| `baseUrl` | `http://localhost:8080/api/v1` |
| `studentId` | UUID sinh viên lấy từ Test 1 |
| `periodId` | UUID đợt đăng ký |
| `sectionId` | UUID lớp học phần |
| `registrationId` | UUID đăng ký (sau khi đăng ký xong) |

Sau khi điền biến, các request trong collection sẽ tự dùng `{{studentId}}`, `{{periodId}}`,...
