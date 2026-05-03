# 💻 HƯỚNG DẪN CHẠY CODE TRÊN VS CODE (TỪ A-Z)

> Hướng dẫn chi tiết từng bước cho người mới. Chạy cả **Backend Spring Boot** và **Frontend** chỉ bằng VS Code.

---

## 🎯 TỔNG QUAN

VS Code có thể chạy được:
- ✅ Backend Spring Boot (Java) — qua Extension Pack for Java
- ✅ Frontend HTML/CSS/JS — qua Live Server
- ✅ 2 cái cùng lúc trong cùng 1 cửa sổ VS Code

---

## 📥 BƯỚC 1: CÀI ĐẶT VS CODE VÀ CÁC EXTENSION

### 1.1. Cài VS Code
Tải về: https://code.visualstudio.com/

### 1.2. Cài JDK 17 (BẮT BUỘC cho Spring Boot)

#### Cách 1: Download trực tiếp
- Tải Eclipse Temurin JDK 17: https://adoptium.net/temurin/releases/?version=17
- Cài đặt bình thường, **TICK CHỌN ô "Set JAVA_HOME variable"**

#### Cách 2: Kiểm tra Java đã cài chưa
Mở **Terminal** (PowerShell/CMD), gõ:
```bash
java -version
```
Phải hiện `openjdk version "17.x.x"` hoặc cao hơn.

Nếu báo `'java' is not recognized...` → Java chưa cài hoặc chưa set PATH.

### 1.3. Cài Maven (BẮT BUỘC)

#### Cách dễ nhất: Dùng Maven Wrapper (không cần cài)
Project có sẵn → mình sẽ thêm `mvnw` vào project, không cần cài Maven riêng.

#### Hoặc cài Maven thủ công:
- Tải: https://maven.apache.org/download.cgi (chọn `apache-maven-3.x.x-bin.zip`)
- Giải nén vào ví dụ `C:\maven`
- Thêm `C:\maven\bin` vào biến môi trường **PATH**
- Kiểm tra: `mvn -version`

---

## 🧩 BƯỚC 2: CÀI 4 EXTENSION TRONG VS CODE

Mở VS Code, click icon **Extensions** bên trái (hoặc nhấn `Ctrl+Shift+X`):

### Bắt buộc cho Backend (Java):
1. **Extension Pack for Java** (Microsoft)
   - Search: `Extension Pack for Java`
   - Click **Install**
   - Đây là gói gồm 6 extension: Language Support, Debugger, Test Runner, Maven, Project Manager, IntelliCode

2. **Spring Boot Extension Pack** (VMware)
   - Search: `Spring Boot Extension Pack`
   - Click **Install**

3. **Lombok Annotations Support** (Microsoft)
   - Search: `Lombok`
   - Click **Install**
   - ⚠️ **QUAN TRỌNG**: Project dùng Lombok (`@Getter`, `@Setter`, `@Builder`...). Nếu không cài sẽ báo lỗi đỏ khắp nơi!

### Bắt buộc cho Frontend:
4. **Live Server** (Ritwick Dey)
   - Search: `Live Server`
   - Click **Install**

### Sau khi cài xong:
- **Đóng VS Code và mở lại** để các extension load đầy đủ.

---

## 📁 BƯỚC 3: MỞ PROJECT TRONG VS CODE

### Cách mở:
1. Mở **VS Code**
2. **File** → **Open Folder...**
3. Chọn thư mục **`group6_retake_registration`** (thư mục root chứa cả `backend/` và `frontend/`)
4. Click **Select Folder**

### VS Code có thể hỏi:
- **"Do you trust the authors?"** → Click **Yes, I trust the authors**
- **"This workspace contains Java projects..."** → Click **Yes**

### Đợi VS Code tải project:
- Góc dưới phải sẽ hiện thông báo **"Importing Maven project..."**
- Đợi 1-3 phút để VS Code tải dependencies
- Khi hoàn tất, biểu tượng Java sẽ ổn định ở góc dưới

### Cấu trúc bạn sẽ thấy bên trái:
```
GROUP6_RETAKE_REGISTRATION
├── backend/
│   ├── src/main/java/com/university/retake/...
│   └── pom.xml
├── database/
├── frontend/
├── postman/
├── README.md
└── ...
```

---

## ⚙️ BƯỚC 4: SỬA CONFIG KẾT NỐI DATABASE

> Trước bước này, bạn phải đã chạy xong 2 file SQL trong SSMS (xem `HUONG_DAN_DATABASE_SSMS.md`).

### 4.1. Mở file `application.properties`
Trong VS Code, mở file:
```
backend/src/main/resources/application.properties
```

### 4.2. Sửa 3 dòng kết nối DB
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=university_retake_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourStrong@Passw0rd
```

### Tùy chọn theo máy bạn:

| Trường hợp | Sửa |
|---|---|
| **SQL Server Express** | Đổi `localhost:1433` → `localhost\\SQLEXPRESS` (chú ý 2 dấu `\\`) |
| **Password khác** | Đổi `password=` thành password thật của bạn |
| **Port khác 1433** | Đổi `localhost:1433` thành `localhost:PORT` |

### Ví dụ cho SQL Server Express:
```properties
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=university_retake_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=12345
```

### 4.3. **Save file** (Ctrl+S)

---

## 🚀 BƯỚC 5: CHẠY BACKEND SPRING BOOT

### Cách 1: Chạy bằng Spring Boot Dashboard (DỄ NHẤT)

1. Click icon **Spring Boot Dashboard** bên trái (icon hình lá xanh)
   - Nếu không thấy → click `...` → **Spring Boot Dashboard**

2. Trong panel **APPS**, sẽ thấy `retake-registration`

3. **Hover** vào dòng đó → click **▶ Start** (icon tam giác xanh)

### Cách 2: Chạy bằng Run Java

1. Mở file `backend/src/main/java/com/university/retake/RetakeRegistrationApplication.java`

2. Phía trên hàm `main(String[] args)` sẽ thấy 2 nút:
   - **Run** ← click cái này
   - **Debug**

3. Click **Run**

### Cách 3: Chạy bằng Terminal trong VS Code

1. Mở Terminal: **View** → **Terminal** (hoặc `Ctrl+\``)

2. Trong terminal:
```bash
cd backend
mvn spring-boot:run
```

> Nếu không có Maven → dùng wrapper: `./mvnw spring-boot:run` (Linux/Mac) hoặc `mvnw.cmd spring-boot:run` (Windows)

### ✅ Backend chạy thành công khi terminal hiện:

```
========================================
  GROUP 6 - RETAKE REGISTRATION API
  Server: http://localhost:8080/api
========================================

Tomcat started on port 8080 (http) with context path '/api'
Started RetakeRegistrationApplication in 5.234 seconds
```

### 🧪 Test backend
Mở trình duyệt: **http://localhost:8080/api/v1/students**

Phải hiện JSON danh sách 3 sinh viên.

### ⚠️ KHÔNG TẮT TERMINAL NÀY — backend phải luôn chạy!

---

## 🌐 BƯỚC 6: CHẠY FRONTEND BẰNG LIVE SERVER

### 6.1. Mở Terminal MỚI (không tắt terminal đang chạy backend)

Trong VS Code, ở Terminal panel dưới cùng:
- Click icon **+** (Add new terminal) bên phải Terminal
- Hoặc **Terminal** → **New Terminal**

### 6.2. Mở file frontend
Click vào file `frontend/index.html` trong cây thư mục bên trái.

### 6.3. Khởi động Live Server
**Cách 1: Click chuột phải trong file**
- Mở file `frontend/index.html`
- **Click chuột phải vào nội dung file** → chọn **Open with Live Server**

**Cách 2: Click chuột phải trong cây thư mục**
- Trong Explorer bên trái, **chuột phải vào file `frontend/index.html`** → **Open with Live Server**

**Cách 3: Status bar góc dưới phải**
- Sau khi cài Live Server, góc dưới phải có nút **"Go Live"**
- Click vào → server tự khởi động

### ✅ Live Server chạy thành công khi:
- Trình duyệt **tự động mở** http://127.0.0.1:5500/frontend/index.html
- Trang web hiện ra với header "Đăng ký học lại — Nhóm 6"
- 2 dropdown ở trên có sẵn dữ liệu sinh viên + đợt đăng ký

---

## 🎬 BƯỚC 7: DEMO FLOW HOẠT ĐỘNG

### Trên trang web vừa mở:

1. **Chọn sinh viên**: SV2024002 — Trần Thị Bình (CNTT-K17A)
2. **Chọn đợt đăng ký**: Đợt 1 - HK1 2025-2026 (Học lại - Ưu tiên)
3. Click nút **Tải dữ liệu**

### Kết quả phải thấy:

**4 thẻ thống kê:**
- Môn cần học lại: **2**
- Tổng tín chỉ học lại: **7** (3 + 4)
- Đã đăng ký: **0**
- Trạng thái: **Đang mở**

**Danh sách 2 môn học lại:**
- Card **IT101** — Nhập môn Lập trình — điểm 2.90 (Trượt)
- Card **IT102** — Cấu trúc dữ liệu và Giải thuật — điểm 4.90 (Trượt)

### Test đăng ký:
1. Click vào card **IT101** để mở rộng
2. Thấy chi tiết điểm thành phần (CC: 5.0, GK: 3.0, CK: 2.5)
3. Thấy lớp **IT101-RT01 — Lớp Học Lại** đang mở
4. Click nút **Đăng ký** màu đỏ
5. Modal xác nhận hiện ra → click **Xác nhận**
6. Toast xanh "Đăng ký thành công!" hiện ở góc trên phải
7. Cuộn xuống bảng **Lịch sử đăng ký** → thấy bản ghi mới có status **APPROVED**
8. Có thể click **Hủy** để hủy đăng ký

---

## ❌ XỬ LÝ CÁC LỖI THƯỜNG GẶP

### Lỗi 1: VS Code không nhận diện project Java
**Triệu chứng:** Mở file `.java` thấy báo lỗi đỏ khắp nơi, không có IntelliSense

**Cách sửa:**
1. Nhấn `Ctrl+Shift+P` → gõ **Java: Clean Java Language Server Workspace** → Enter → Restart and Delete
2. Đợi VS Code tải lại (mất 2-5 phút)
3. Nếu vẫn lỗi → kiểm tra JDK 17: `Ctrl+Shift+P` → **Java: Configure Java Runtime**

### Lỗi 2: Lombok báo lỗi `getXxx() is undefined`
**Triệu chứng:** Code dùng `@Getter`, `@Builder` nhưng IDE không nhận

**Cách sửa:**
1. Cài extension **Lombok Annotations Support** (xem Bước 2)
2. Restart VS Code hoàn toàn
3. Vẫn lỗi → mở `Command Palette` (Ctrl+Shift+P) → **Java: Clean Java Language Server Workspace**

### Lỗi 3: `Failed to configure a DataSource: 'url' attribute is not specified`
**Triệu chứng:** Backend khởi động báo lỗi connect DB

**Cách sửa:**
1. Kiểm tra file `application.properties` đã sửa đúng chưa
2. Kiểm tra SQL Server có đang chạy không (Services → SQL Server (MSSQLSERVER) → Running)
3. Test connect bằng SSMS xem có vào được không

### Lỗi 4: `Communications link failure` / `Connection refused`
**Triệu chứng:** Backend không kết nối được SQL Server

**Cách sửa:**
1. Mở **SQL Server Configuration Manager**
2. SQL Server Network Configuration → Protocols for MSSQLSERVER → **Enable TCP/IP**
3. Vào tab IP Addresses → IPAll → TCP Port = 1433
4. Restart **SQL Server (MSSQLSERVER)** trong Services
5. Tắt Windows Firewall thử (chỉ để test)

### Lỗi 5: `Login failed for user 'sa'`
**Cách sửa:**
1. SSMS → click chuột phải vào tên server → **Properties** → **Security** → tick **SQL Server and Windows Authentication mode**
2. Restart SQL Server service
3. SSMS → Security → Logins → chuột phải `sa` → **Properties**
   - Tab General: nhập password mới
   - Tab Status: tick **Enabled**
4. Update lại password trong `application.properties`

### Lỗi 6: `Port 8080 was already in use`
**Cách sửa:**
- **Cách 1:** Tìm và kill process đang dùng port 8080
  ```bash
  # Windows:
  netstat -ano | findstr :8080
  taskkill /PID <PID> /F
  ```
- **Cách 2:** Đổi port trong `application.properties`:
  ```properties
  server.port=9090
  ```
  Và sửa `frontend/js/api.js`:
  ```javascript
  const API_BASE = 'http://localhost:9090/api/v1';
  ```

### Lỗi 7: Frontend không gọi được API (CORS error)
**Triệu chứng:** Console trình duyệt báo `CORS policy: No 'Access-Control-Allow-Origin'`

**Cách sửa:**
- Đảm bảo bạn dùng **Live Server** thay vì mở trực tiếp file HTML (file://...)
- File `CorsConfig.java` đã có sẵn cấu hình CORS, nhưng cần backend chạy lại sau khi cài

### Lỗi 8: Dropdown sinh viên trống (không có data)
**Cách sửa:**
1. Mở **Console** trình duyệt (F12 → Console)
2. Xem có lỗi `fetch failed` không?
3. Test API trực tiếp: http://localhost:8080/api/v1/students
4. Nếu API ok mà dropdown trống → kiểm tra biến `API_BASE` trong `js/api.js`

### Lỗi 9: `mvn` command not found
**Cách sửa:**
- Cài Maven (xem Bước 1.3) hoặc dùng Maven Wrapper:
  ```bash
  cd backend
  ./mvnw spring-boot:run     # Linux/Mac
  mvnw.cmd spring-boot:run   # Windows
  ```

---

## 🔥 SHORTCUT VS CODE HỮU ÍCH

| Phím tắt | Chức năng |
|---|---|
| `Ctrl+\`` | Mở/đóng terminal |
| `Ctrl+Shift+P` | Command Palette (gõ lệnh) |
| `Ctrl+P` | Mở file nhanh |
| `F5` | Run/Debug |
| `Ctrl+Shift+F5` | Restart debug |
| `Ctrl+/` | Comment dòng code |
| `Alt+Shift+F` | Format code |
| `Ctrl+B` | Ẩn/hiện sidebar |

---

## ✅ CHECKLIST HOÀN THÀNH

### Phần cài đặt
- [ ] Đã cài JDK 17 (`java -version` trả về 17.x)
- [ ] Đã cài VS Code
- [ ] Đã cài 4 extension: Extension Pack for Java, Spring Boot Extension Pack, Lombok, Live Server
- [ ] Đã restart VS Code sau khi cài extension

### Phần database
- [ ] Đã chạy 2 file SQL trong SSMS
- [ ] Test query `SELECT * FROM students` ra 3 dòng

### Phần code
- [ ] Mở project `group6_retake_registration` trong VS Code
- [ ] Đợi Maven import xong (góc dưới phải không còn loading)
- [ ] Đã sửa `application.properties` cho khớp DB
- [ ] Backend chạy được, terminal hiện "Started RetakeRegistrationApplication"
- [ ] Mở http://localhost:8080/api/v1/students thấy JSON

### Phần frontend
- [ ] Live Server chạy được
- [ ] Trang web hiện http://127.0.0.1:5500
- [ ] Dropdown có sẵn data
- [ ] Test thử chọn sinh viên Bình → load được 2 môn học lại
- [ ] Đăng ký được 1 lớp → hiện trong bảng lịch sử

---

## 🎯 SƠ ĐỒ HOẠT ĐỘNG KHI TEST

```
[Trình duyệt: localhost:5500]              [VS Code Terminal: Backend]
       │                                              │
       │  GET /api/v1/students                        │
       ├─────────────────────────────────────────────▶│
       │                                              ├──▶ [SQL Server: localhost:1433]
       │                                              │       │
       │                                              │◀──────┤ Trả về data
       │  JSON response                               │
       │◀─────────────────────────────────────────────┤
       │                                              │
[Hiển thị dropdown sinh viên]
```

---

## 🚀 KỊCH BẢN DEMO HOÀN CHỈNH (cho thuyết trình)

### Mở 2 terminal cùng lúc trong VS Code:
```
[Terminal 1]                    [Terminal 2]
cd backend                      → Live Server tự chạy bằng GUI
mvn spring-boot:run                (không cần command)
```

### Mở 3 cửa sổ trình duyệt:
- **Tab 1:** http://127.0.0.1:5500/frontend/index.html (Frontend)
- **Tab 2:** http://localhost:8080/api/v1/students (Test API GET)
- **Tab 3:** Postman với collection (Test API POST)

### Mở SSMS bên cạnh:
- Cửa sổ SSMS để query trực tiếp DB minh họa

### Demo flow:
1. **Mở Frontend** → cho xem giao diện đẹp
2. **Chọn Bình + đợt** → Click Tải → xem 2 môn học lại
3. **Click vào card IT101** → giải thích cách tính điểm tổng kết = 2.9 < 5 ⇒ Failed
4. **Click Đăng ký** → xác nhận → đăng ký thành công
5. **Quay sang SSMS** → query `SELECT * FROM course_registrations` → thấy bản ghi mới
6. **Quay sang Postman** → demo API GET /retake/students/{id}/courses → giải thích logic JSON

---

## 📚 TÀI LIỆU LIÊN QUAN
- `HUONG_DAN_DATABASE_SSMS.md` — Tạo data trong SSMS
- `HUONG_DAN_POSTMAN.md` — Test API trong Postman
- `README.md` — Tổng quan project
