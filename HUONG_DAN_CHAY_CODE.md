# 🚀 HƯỚNG DẪN CHẠY CODE (BACKEND + FRONTEND)

> Sau khi đã setup database trong SSMS xong (xem file `HUONG_DAN_DATABASE_SSMS.md`).

---

## 📦 BƯỚC 1: CHẠY BACKEND (SPRING BOOT)

### Yêu cầu cài đặt
- **Java 17** (kiểm tra: `java -version`)
- **Maven 3.6+** (kiểm tra: `mvn -version`)
- **IDE khuyến nghị**: IntelliJ IDEA hoặc VS Code với Java Extension Pack

### Cách 1: Chạy bằng IntelliJ IDEA (dễ nhất)
1. Mở IntelliJ IDEA → **File** → **Open**
2. Chọn thư mục `backend/` (chứa file `pom.xml`)
3. Đợi IDE tải dependencies (xem góc dưới phải)
4. Mở file `RetakeRegistrationApplication.java`
5. Click nút **▶ Run** (hoặc nhấn `Shift + F10`)

### Cách 2: Chạy bằng VS Code
1. Mở VS Code → **File** → **Open Folder** → chọn `backend/`
2. Cài extension **Extension Pack for Java** nếu chưa có
3. Mở file `RetakeRegistrationApplication.java`
4. Click **Run** ở phía trên hàm `main()`

### Cách 3: Chạy bằng terminal/cmd
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### ✅ Backend chạy thành công khi thấy:
```
==========================================
  GROUP 6 - RETAKE REGISTRATION API
  Server: http://localhost:8080/api
==========================================

Tomcat started on port 8080 (http) with context path '/api'
```

### 🧪 Test nhanh
Mở trình duyệt: http://localhost:8080/api/v1/students

Nếu hiện JSON danh sách sinh viên → ✅ thành công.

### ❌ Lỗi thường gặp

| Lỗi | Cách sửa |
|---|---|
| `Cannot connect to database` | Kiểm tra SQL Server đang chạy, xem `HUONG_DAN_DATABASE_SSMS.md` Bước 6-7 |
| `Port 8080 already in use` | Đổi port trong `application.properties`: `server.port=8081` |
| `Login failed for user 'sa'` | Sai password trong `application.properties`, sửa lại cho khớp với SSMS |
| `Driver class not found` | Chạy `mvn clean install` lại |
| `Cannot find symbol` | Chưa cài Lombok plugin trong IDE → cài plugin Lombok |

---

## 🎨 BƯỚC 2: CHẠY FRONTEND

Frontend là HTML/CSS/JS thuần → có nhiều cách chạy:

### Cách 1: Mở trực tiếp file HTML (nhanh nhất)
1. Vào thư mục `frontend/`
2. Click đúp file `index.html` → mở trên trình duyệt

> ⚠️ Nếu thấy lỗi CORS hoặc fetch không được → dùng **Cách 2**.

### Cách 2: Live Server (VS Code) — KHUYẾN NGHỊ
1. Mở VS Code, mở thư mục `frontend/`
2. Cài extension **Live Server** (tác giả: Ritwick Dey)
3. Chuột phải vào `index.html` → chọn **Open with Live Server**
4. Trình duyệt tự mở http://127.0.0.1:5500

### Cách 3: Python HTTP Server
```bash
cd frontend
# Python 3:
python -m http.server 5500
# Python 2:
python -m SimpleHTTPServer 5500
```
Mở: http://localhost:5500

### Cách 4: Node.js http-server
```bash
npm install -g http-server
cd frontend
http-server -p 5500
```

### ✅ Frontend chạy thành công khi:
- Trang web hiện ra với header "Đăng ký học lại"
- 2 dropdown ở trên (Sinh viên + Đợt đăng ký) **hiện tự động** danh sách
- Nếu dropdown trống → backend chưa chạy hoặc lỗi CORS

---

## 🎬 BƯỚC 3: DEMO FLOW HOẠT ĐỘNG

1. **Mở web** → 2 dropdown đã có sẵn data
2. Chọn sinh viên: **SV2024002 — Trần Thị Bình** (Bình có 2 môn cần học lại)
3. Chọn đợt: **Đợt 1 - HK1 2025-2026 (Học lại - Ưu tiên)**
4. Click nút **Tải dữ liệu**
5. Sẽ thấy:
   - Thẻ thống kê: "Môn cần học lại = **2**", "Tổng tín chỉ = **7**"
   - 2 card hiện ra: **IT101** (điểm 2.90) và **IT102** (điểm 4.90)
6. Click vào card **IT101** để mở rộng:
   - Thấy chi tiết điểm thành phần (CC, GK, CK)
   - Thấy lớp **IT101-RT01 (Lớp Học Lại)** đang mở
   - Click nút **Đăng ký** màu đỏ
7. Modal xác nhận hiện ra → click **Xác nhận**
8. Toast xanh hiện "Đăng ký thành công!"
9. Cuộn xuống bảng **Lịch sử đăng ký** → thấy bản ghi vừa tạo
10. Có thể click **Hủy** để hủy đăng ký

---

## 🔧 STRUCTURE OVERVIEW

```
backend/                                    ← Code chạy ở port 8080
├── pom.xml                                 ← Dependencies (Spring Boot, JPA, SQL Server)
└── src/main/
    ├── java/com/university/retake/
    │   ├── RetakeRegistrationApplication.java  ← Entry point
    │   ├── config/CorsConfig.java          ← Cho phép frontend gọi
    │   ├── entity/                         ← Mapping với 7 bảng DB
    │   ├── repository/                     ← Truy vấn DB
    │   ├── service/
    │   │   ├── RetakeService.java          ← ⭐ Logic tính điểm pass/fail
    │   │   └── RegistrationService.java    ← Logic đăng ký
    │   ├── controller/                     ← 6 REST endpoints
    │   └── dto/                            ← Data transfer objects
    └── resources/application.properties    ← ⚙️ Config kết nối DB

frontend/                                   ← Web tĩnh, mở bằng trình duyệt
├── index.html                              ← Trang chính
├── css/styles.css                          ← Editorial design
└── js/
    ├── api.js                              ← Gọi API backend
    └── app.js                              ← UI logic
```

---

## ⚙️ CẤU HÌNH NẾU BACKEND CHẠY KHÁC PORT

Mặc định backend chạy ở `http://localhost:8080/api`.

### Nếu đổi port backend:
1. Sửa `backend/src/main/resources/application.properties`:
   ```properties
   server.port=9090
   ```
2. Sửa `frontend/js/api.js` dòng đầu:
   ```javascript
   const API_BASE = 'http://localhost:9090/api/v1';
   ```

---

## 📚 TÀI LIỆU LIÊN QUAN

| File | Nội dung |
|---|---|
| `HUONG_DAN_DATABASE_SSMS.md` | Cách tạo database trong SQL Server Management Studio |
| `HUONG_DAN_POSTMAN.md` | Cách test API trên Postman |
| `README.md` | Tổng quan project |

---

## 🎓 KỊCH BẢN DEMO HOÀN CHỈNH (cho thuyết trình)

### Slide 1: Database
- Mở SSMS, chạy `01_create_database.sql` → tạo bảng
- Chạy `02_seed_data.sql` → có dữ liệu
- Chạy query "SELECT FROM students" → hiện 3 sinh viên

### Slide 2: Backend
- Mở terminal, `mvn spring-boot:run`
- Mở browser: http://localhost:8080/api/v1/students → JSON

### Slide 3: API Postman
- Mở Postman, chạy API "GET retake courses"
- Giải thích logic: tính tổng điểm → lọc < 5.0 → trả về kèm lớp đang mở

### Slide 4: Frontend
- Mở http://localhost:5500
- Chọn sinh viên Bình → click Tải dữ liệu
- Thấy 2 môn cần học lại
- Click vào card → xem điểm thành phần
- Click Đăng ký → đăng ký thành công

### Slide 5: Validate trong DB
- Quay lại SSMS, chạy:
  ```sql
  SELECT * FROM course_registrations ORDER BY registered_at DESC;
  ```
  → Thấy bản ghi vừa tạo, `status = 'APPROVED'`
- Thấy `course_sections.current_students` đã tăng thêm 1
