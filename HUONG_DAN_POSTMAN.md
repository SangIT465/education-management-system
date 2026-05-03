# 📮 HƯỚNG DẪN TEST API TRÊN POSTMAN

> Hướng dẫn từng bước test API của Nhóm 6. Yêu cầu: Backend Spring Boot đã chạy ở `http://localhost:8080`.

---

## 🔧 BƯỚC 0: CHUẨN BỊ

### Cài đặt Postman
- Download: https://www.postman.com/downloads/
- Tạo tài khoản miễn phí (hoặc dùng chế độ guest)

### Đảm bảo Backend đang chạy
Mở terminal trong thư mục `backend`, chạy:
```bash
mvn spring-boot:run
```

Khi thấy log:
```
========================================
  GROUP 6 - RETAKE REGISTRATION API
  Server: http://localhost:8080/api
========================================
```
✅ Backend đã sẵn sàng. **GIỮ NGUYÊN cửa sổ này, không tắt**.

### Test nhanh server bằng trình duyệt
Mở trình duyệt, vào: http://localhost:8080/api/v1/students

Nếu hiện JSON danh sách sinh viên → server OK. Nếu lỗi → kiểm tra lại backend & database.

---

## 📥 BƯỚC 1: IMPORT COLLECTION VÀO POSTMAN

### Cách 1: Import file có sẵn
1. Mở **Postman**
2. Click nút **Import** (góc trên trái)
3. Kéo thả file `postman/Group6_RetakeRegistration.postman_collection.json` vào
4. Click **Import**

✅ Bên trái sẽ thấy collection **"Group 6 - Retake Registration API"** với 3 nhóm:
- 1. NHÓM CHỨC NĂNG 1 - Danh sách môn học lại
- 2. NHÓM CHỨC NĂNG 2 - Đăng ký học phần
- 3. NHÓM CHỨC NĂNG 3 - Lịch sử đăng ký

### Cách 2: Tạo thủ công (nếu không import được)
1. Click **+ Workspace** → **Create Workspace** → đặt tên `Group 6`
2. Trong workspace, click **+ New Collection** → đặt tên `Retake Registration`
3. Tạo từng request theo hướng dẫn bên dưới

---

## 🔑 BƯỚC 2: CẤU HÌNH BIẾN MÔI TRƯỜNG (VARIABLES)

Postman có "biến" để dùng chung cho nhiều request. Setup ngay từ đầu:

### Cách làm:
1. Click vào tên collection **"Group 6 - Retake Registration API"**
2. Vào tab **Variables**
3. Sẽ thấy có sẵn các biến:

| KEY | INITIAL VALUE | CURRENT VALUE | Mô tả |
|---|---|---|---|
| `baseUrl` | `http://localhost:8080/api/v1` | `http://localhost:8080/api/v1` | URL gốc |
| `studentId` | `PASTE-STUDENT-UUID-HERE` | (sửa sau) | ID sinh viên |
| `periodId` | `PASTE-PERIOD-UUID-HERE` | (sửa sau) | ID đợt đăng ký |
| `sectionId` | `PASTE-SECTION-UUID-HERE` | (sửa sau) | ID lớp học phần |
| `registrationId` | (để trống) | (sửa sau) | ID đăng ký |

⚠️ Click **Save** sau khi sửa.

> **Cách dùng biến**: Trong URL hoặc body, viết `{{baseUrl}}` hoặc `{{studentId}}` để Postman tự thay thế.

---

## 🧪 BƯỚC 3: TEST CÁC API THEO THỨ TỰ

### ▶️ TEST 1: Lấy danh sách sinh viên

**Mục đích**: Lấy danh sách sinh viên trong database, **đặc biệt là `id` của Bình** để dùng cho các API sau.

#### Cách tạo request:
- **Method**: `GET`
- **URL**: `{{baseUrl}}/students`

#### Cách chạy:
1. Click request **"GET - Lấy danh sách sinh viên"**
2. Click nút **Send** màu xanh
3. Xem kết quả ở phần **Body** bên dưới

#### Kết quả mong đợi (status `200 OK`):
```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "id": "12345678-aaaa-bbbb-cccc-1234567890ab",
      "studentCode": "SV2024001",
      "fullName": "Nguyễn Văn An",
      "className": "CNTT-K17A",
      "email": "an.nv@uni.edu.vn"
    },
    {
      "id": "87654321-dddd-eeee-ffff-0987654321cd",
      "studentCode": "SV2024002",
      "fullName": "Trần Thị Bình",
      "className": "CNTT-K17A",
      "email": "binh.tt@uni.edu.vn"
    }
  ]
}
```

#### 📌 LÀM NGAY:
- **Copy giá trị `id` của Trần Thị Bình** (sinh viên có 2 môn cần học lại)
- Vào tab **Variables** của collection → Paste vào ô `studentId` (cả Initial và Current value) → **Save**

---

### ▶️ TEST 2: Lấy đợt đăng ký đang mở

**Mục đích**: Lấy `id` của đợt đăng ký để dùng khi tạo đăng ký.

#### Cấu hình:
- **Method**: `GET`
- **URL**: `{{baseUrl}}/registration-periods/open`

#### Click **Send** → Kết quả mong đợi:
```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "id": "abc12345-...",
      "name": "Đợt 1 - HK1 2025-2026 (Học lại - Ưu tiên)",
      "startTime": "2025-08-01T00:00:00",
      "endTime": "2026-08-10T00:00:00",
      "maxCredits": 25,
      "minCredits": 0,
      "allowRetake": true,
      "semesterName": "Học kỳ 1 - 2025-2026"
    }
  ]
}
```

#### 📌 LÀM NGAY:
- Copy `id` của đợt **"Học lại - Ưu tiên"** → paste vào biến `periodId` → **Save**

---

### ▶️ TEST 3: ⭐ LẤY DANH SÁCH MÔN CẦN HỌC LẠI (API CHÍNH)

**Mục đích**: Đây là **API quan trọng nhất** — màn hình "danh sách môn học lại của sinh viên" như đề bài yêu cầu.

#### Cấu hình:
- **Method**: `GET`
- **URL**: `{{baseUrl}}/retake/students/{{studentId}}/courses`

#### Click **Send** → Kết quả mong đợi (cho sinh viên Bình):

```json
{
  "success": true,
  "message": "Lấy danh sách môn học lại thành công. Tìm thấy 2 môn.",
  "data": [
    {
      "studentCourseSectionId": "...",
      "courseId": "...",
      "courseCode": "IT101",
      "courseName": "Nhập môn Lập trình",
      "credits": 3,
      "semesterName": "Học kỳ 1 - 2024-2025",
      "totalScore": 2.90,
      "gradeStatus": "FAILED",
      "gradeComponents": [
        {
          "componentCode": "CC",
          "componentName": "Chuyên cần",
          "weightPercentage": 10.00,
          "score": 5.00
        },
        {
          "componentCode": "GK",
          "componentName": "Giữa kỳ",
          "weightPercentage": 30.00,
          "score": 3.00
        },
        {
          "componentCode": "CK",
          "componentName": "Cuối kỳ",
          "weightPercentage": 60.00,
          "score": 2.50
        }
      ],
      "availableSections": [
        {
          "sectionId": "xyz-section-id",
          "sectionCode": "IT101-RT01",
          "sectionName": "IT101 - Lớp Học Lại",
          "classType": "Lớp học lại",
          "maxStudents": 30,
          "currentStudents": 5,
          "remainingSlots": 25,
          "status": "open",
          "semesterName": "Học kỳ 2 - 2024-2025"
        }
      ]
    },
    {
      "courseCode": "IT102",
      "totalScore": 4.90,
      "gradeStatus": "FAILED",
      ...
    }
  ]
}
```

#### 📌 LÀM NGAY:
- Tìm **`sectionId`** của lớp `IT101-RT01` (trong mảng `availableSections`)
- Copy → paste vào biến `sectionId` → **Save**

#### 🎯 Giải thích logic API này:
1. Lấy tất cả môn `student_course_sections` có `status = 'completed'` của Bình
2. Với mỗi môn, tính: `totalScore = Σ(score × weight / 100)`
3. Lọc các môn có `totalScore < 5.0` → đó là môn cần học lại
4. Với mỗi môn cần học lại, tìm các `course_sections` đang mở (`status = 'open'`) cho môn đó

---

### ▶️ TEST 4: ĐĂNG KÝ HỌC LẠI

**Mục đích**: Đăng ký Bình vào lớp `IT101-RT01`.

#### Cấu hình:
- **Method**: `POST`
- **URL**: `{{baseUrl}}/registrations`
- **Tab Headers**:
  - Key: `Content-Type` | Value: `application/json`
- **Tab Body**:
  - Chọn radio **raw** → dropdown phải chọn **JSON**
  - Dán nội dung sau:

```json
{
  "studentId": "{{studentId}}",
  "courseSectionId": "{{sectionId}}",
  "registrationPeriodId": "{{periodId}}",
  "registrationType": "RETAKE",
  "note": "Đăng ký học lại môn IT101"
}
```

#### Click **Send** → Kết quả mong đợi:
```json
{
  "success": true,
  "message": "Đăng ký học phần thành công",
  "data": {
    "id": "registration-uuid-here",
    "studentCode": "SV2024002",
    "studentName": "Trần Thị Bình",
    "sectionCode": "IT101-RT01",
    "courseName": "Nhập môn Lập trình",
    "credits": 3,
    "registrationType": "RETAKE",
    "status": "APPROVED",
    "registeredAt": "2026-05-03T10:30:00",
    "note": "Đăng ký học lại môn IT101"
  }
}
```

#### 📌 LÀM NGAY:
- Copy `id` từ response → paste vào biến `registrationId` → **Save**

#### ❌ Các lỗi có thể gặp:
| Lỗi | Nguyên nhân | Cách sửa |
|---|---|---|
| `"Sinh viên đã đăng ký lớp này rồi"` | Đã chạy API này 1 lần rồi | Bỏ qua, chuyển bước tiếp |
| `"Lớp học phần đã đầy"` | `current_students >= max_students` | Tăng `max_students` trong DB |
| `"Đợt đăng ký đã đóng"` | `is_open = 0` | UPDATE `is_open = 1` trong DB |
| `"Không tìm thấy sinh viên"` | `studentId` sai | Kiểm tra lại biến |

---

### ▶️ TEST 5: XEM LỊCH SỬ ĐĂNG KÝ

**Mục đích**: Xem các đăng ký đã thực hiện của Bình.

#### Cấu hình:
- **Method**: `GET`
- **URL**: `{{baseUrl}}/registrations/students/{{studentId}}`

#### Click **Send** → Kết quả mong đợi:
```json
{
  "success": true,
  "message": "Tìm thấy 1 đăng ký",
  "data": [
    {
      "id": "...",
      "sectionCode": "IT101-RT01",
      "courseName": "Nhập môn Lập trình",
      "credits": 3,
      "registrationType": "RETAKE",
      "status": "APPROVED",
      "registeredAt": "2026-05-03T10:30:00"
    }
  ]
}
```

---

### ▶️ TEST 6: HỦY ĐĂNG KÝ

**Mục đích**: Hủy đăng ký vừa tạo.

#### Cấu hình:
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/registrations/{{registrationId}}`

#### Click **Send** → Kết quả mong đợi:
```json
{
  "success": true,
  "message": "Hủy đăng ký thành công",
  "data": {
    "id": "...",
    "status": "CANCELED",
    ...
  }
}
```

#### 📌 Kiểm tra trong SSMS:
```sql
SELECT status, is_active FROM course_registrations
WHERE id = 'registration-id-vừa-hủy';
```
→ `status` phải là `'CANCELED'` và `is_active = 0`.

Đồng thời `course_sections.current_students` phải giảm 1.

---

## 📑 BƯỚC 4: TỔNG KẾT FLOW TEST

| Thứ tự | Test | Phương thức | URL | Mục đích |
|---|---|---|---|---|
| 1 | Lấy SV | GET | `/students` | Lấy `studentId` |
| 2 | Lấy đợt | GET | `/registration-periods/open` | Lấy `periodId` |
| 3 | ⭐ Môn học lại | GET | `/retake/students/{id}/courses` | Lấy `sectionId` |
| 4 | Đăng ký | POST | `/registrations` | Đăng ký học lại |
| 5 | Lịch sử | GET | `/registrations/students/{id}` | Xem đã đăng ký |
| 6 | Hủy | DELETE | `/registrations/{id}` | Hủy đăng ký |

---

## 🎯 TEST CASE NÂNG CAO

### Test với sinh viên không có môn nào trượt
- Đổi biến `studentId` sang `id` của **Nguyễn Văn An** (có 1 môn trượt nhưng test riêng)
- Hoặc của **Lê Hoàng Cường** (chưa có lịch sử)
- Chạy lại Test 3 → mảng `data` phải rỗng `[]`

### Test đăng ký trùng
1. Chạy Test 4 lần đầu → thành công
2. Chạy Test 4 lần thứ 2 với cùng dữ liệu → phải báo lỗi `"Sinh viên đã đăng ký lớp này rồi"`

### Test đợt đăng ký đóng
1. Trong SSMS chạy:
```sql
UPDATE registration_periods SET is_open = 0 WHERE name LIKE N'%Học lại%';
```
2. Chạy Test 4 → phải báo lỗi `"Đợt đăng ký đã đóng"`
3. Restore:
```sql
UPDATE registration_periods SET is_open = 1 WHERE name LIKE N'%Học lại%';
```

---

## ❌ XỬ LÝ LỖI

### Lỗi 1: `Could not get any response`
- Backend chưa chạy. Mở terminal `cd backend && mvn spring-boot:run`
- Hoặc kiểm tra port 8080 có bị chiếm: `netstat -ano | findstr :8080`

### Lỗi 2: `404 Not Found`
- URL sai. Kiểm tra `{{baseUrl}}` đã được set chưa.
- Kiểm tra context-path: phải là `/api/v1/...`

### Lỗi 3: `400 Bad Request - studentId không được trống`
- Biến `{{studentId}}` chưa được set hoặc set sai.
- Vào tab Variables → kiểm tra cả Initial Value và Current Value đều có giá trị thực tế.

### Lỗi 4: `500 Internal Server Error`
- Xem log ở terminal đang chạy backend → tìm chi tiết exception
- Thường do: database chưa connect, dữ liệu thiếu, query JPA sai

### Lỗi 5: `Variable {{studentId}} chưa được resolve`
- Lưu lại sau khi sửa biến (Ctrl+S hoặc nút Save)
- Kiểm tra **scope** của biến: phải là **Collection variable**, không phải Environment variable

---

## ✅ CHECKLIST HOÀN THÀNH

- [ ] Backend Spring Boot đang chạy ở port 8080
- [ ] Đã import collection vào Postman
- [ ] Đã set 4 biến: `baseUrl`, `studentId`, `periodId`, `sectionId`
- [ ] Test 1 (GET students) → trả về danh sách
- [ ] Test 2 (GET periods) → trả về đợt đăng ký
- [ ] Test 3 (GET retake courses) → trả về môn cần học lại có chi tiết điểm + lớp đang mở
- [ ] Test 4 (POST register) → tạo đăng ký thành công
- [ ] Test 5 (GET history) → thấy đăng ký vừa tạo
- [ ] Test 6 (DELETE cancel) → hủy thành công, status đổi thành CANCELED

---

## 💡 MẸO HAY

### Lưu response làm biến tự động
Vào tab **Tests** của một request và thêm script:
```javascript
const json = pm.response.json();
if (json.data && json.data[0]) {
  pm.collectionVariables.set("studentId", json.data[0].id);
  console.log("Đã set studentId =", json.data[0].id);
}
```
→ Mỗi lần chạy GET students sẽ tự cập nhật `studentId`.

### Chạy tất cả request một lần
- Click chuột phải vào collection → **Run collection**
- Chọn các request muốn chạy → **Run**

### Xem console log
- Click **Console** (góc dưới trái) để xem chi tiết request/response.

---

## 📖 GHI CHÚ

- Postman tự động lưu lịch sử request ở tab **History**
- Có thể xuất collection để chia sẻ: chuột phải → **Export**
- Để demo trên máy khác: import file `.postman_collection.json` là chạy được ngay
