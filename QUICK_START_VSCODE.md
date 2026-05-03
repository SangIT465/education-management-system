# ⚡ QUICK START — CHẠY CODE TRÊN VS CODE (5 PHÚT)

> Phiên bản rút gọn. Đã có sẵn database trong SSMS rồi.

---

## ✅ CHECKLIST TRƯỚC KHI BẮT ĐẦU

- [ ] Đã cài **JDK 17** (test: `java -version`)
- [ ] Đã cài **VS Code**
- [ ] SQL Server đang chạy + đã có database `university_retake_db` với data mẫu

---

## 📋 5 BƯỚC

### Bước 1: Cài 4 Extension trong VS Code (1 phút)

Mở VS Code → `Ctrl+Shift+X` → cài lần lượt:

1. **Extension Pack for Java** (Microsoft) ⭐
2. **Spring Boot Extension Pack** (VMware) ⭐
3. **Lombok Annotations Support** ⭐⭐⭐ (BẮT BUỘC, nếu không sẽ lỗi đỏ)
4. **Live Server** (Ritwick Dey)

→ **Đóng và mở lại VS Code**

> **Mẹo:** Khi mở project lần đầu, VS Code sẽ tự gợi ý cài 4 extension này (đã được khai báo trong `.vscode/extensions.json`). Click **Install All**.

---

### Bước 2: Mở Project (30 giây)

```
File → Open Folder → chọn thư mục: group6_retake_registration
```

Khi VS Code hỏi "Do you trust the authors?" → **Yes**

Đợi góc dưới phải hiện **"Importing Maven project..."** kết thúc (1-3 phút).

---

### Bước 3: Sửa Config Database (30 giây)

Mở file: `backend/src/main/resources/application.properties`

Sửa 3 dòng cho khớp SQL Server của bạn:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=university_retake_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourStrong@Passw0rd
```

**Lưu file:** `Ctrl+S`

---

### Bước 4: Chạy Backend (30 giây)

#### Cách dễ nhất:
1. Mở file `RetakeRegistrationApplication.java` (đường dẫn: `backend/src/main/java/com/university/retake/`)
2. Phía trên hàm `main()` có chữ **▶ Run** màu xanh — **Click vào**

#### Hoặc nhấn `F5` để Debug

✅ **Thành công khi terminal hiện:**
```
========================================
  GROUP 6 - RETAKE REGISTRATION API
  Server: http://localhost:8080/api
========================================
```

🧪 **Test:** Mở trình duyệt http://localhost:8080/api/v1/students → phải hiện JSON

> **GIỮ NGUYÊN terminal này, KHÔNG TẮT!**

---

### Bước 5: Chạy Frontend (30 giây)

1. Trong VS Code, mở file `frontend/index.html`

2. **Click chuột phải** vào file → chọn **Open with Live Server**

   *Hoặc click nút **"Go Live"** ở góc dưới phải VS Code*

3. Trình duyệt tự mở: http://127.0.0.1:5500/frontend/index.html

✅ **Thành công khi:** Trang web hiện ra, 2 dropdown có sẵn dữ liệu sinh viên

---

## 🎬 TEST NGAY

1. Chọn **SV2024002 — Trần Thị Bình**
2. Chọn **Đợt 1 - HK1 2025-2026**
3. Click **Tải dữ liệu**
4. Thấy 2 môn cần học lại: **IT101** và **IT102** ✅
5. Click vào card IT101 → mở rộng → thấy chi tiết điểm và lớp đang mở
6. Click **Đăng ký** → xác nhận → "Đăng ký thành công!" 🎉

---

## ❌ NẾU GẶP LỖI

| Lỗi | Cách sửa nhanh |
|---|---|
| Code Java đỏ khắp nơi | Cài Lombok extension + Restart VS Code |
| `Failed to configure DataSource` | Sửa lại `application.properties` |
| `Connection refused` | Bật TCP/IP trong SQL Server Configuration Manager |
| `Login failed for user 'sa'` | Sai password, hoặc bật Mixed Mode trong SSMS |
| Port 8080 đã dùng | Đổi `server.port=9090` trong `application.properties` |
| Frontend trống dropdown | Backend chưa chạy, kiểm tra terminal |

→ Chi tiết hơn xem **`HUONG_DAN_VSCODE.md`**

---

## 🎯 GIAO DIỆN VS CODE KHI ĐANG CHẠY

```
┌─ VS Code ─────────────────────────────────────────────────┐
│ [Explorer]   [Editor: RetakeRegistrationApplication.java] │
│              [▶ Run | Debug] ← click ở đây                │
│  backend/                                                  │
│  frontend/                                                 │
│  database/                                                 │
│                                                            │
├────────────────────────────────────────────────────────────┤
│ TERMINAL                            [+] [×]                │
│                                                            │
│ Tomcat started on port 8080 ← Backend đang chạy!          │
│                                                            │
└────────────────────────────────────────────────────────────┘
        ↓
  [Trình duyệt: http://127.0.0.1:5500/frontend/index.html]
                    ↑ Live Server tự mở
```

---

## 📁 CÁC FILE QUAN TRỌNG

| File | Khi nào sửa |
|---|---|
| `backend/src/main/resources/application.properties` | Đổi config DB |
| `frontend/js/api.js` | Đổi URL backend nếu chạy port khác |
| `.vscode/launch.json` | Cấu hình debug (đã có sẵn) |

Xong! 🎉
