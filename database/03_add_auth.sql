-- ============================================================
-- MIGRATION 03: Tạo bảng accounts cho xác thực
-- Chạy file này SAU khi đã chạy 01_create_database.sql và 02_seed_data.sql
-- ============================================================
USE university_retake_db;
GO

-- Xóa cột password khỏi students nếu đã thêm trước đó
IF EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'students' AND COLUMN_NAME = 'password'
)
BEGIN
    ALTER TABLE students DROP COLUMN password;
    PRINT N'Đã xóa cột password khỏi bảng students';
END
GO

-- Tạo bảng accounts
IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'accounts'
)
BEGIN
    CREATE TABLE accounts (
        id           UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID(),
        username     VARCHAR(50)      NOT NULL,
        password     VARCHAR(255)     NOT NULL,
        role         VARCHAR(20)      NOT NULL DEFAULT 'student', -- 'student' hoặc 'admin'
        student_id   UNIQUEIDENTIFIER NULL,
        full_name    NVARCHAR(255)    NOT NULL,
        is_active    BIT              NOT NULL DEFAULT 1,
        created_at   DATETIME         NOT NULL DEFAULT GETDATE(),

        CONSTRAINT PK_accounts PRIMARY KEY (id),
        CONSTRAINT UQ_accounts_username UNIQUE (username),
        CONSTRAINT FK_accounts_student  FOREIGN KEY (student_id) REFERENCES students(id),
        CONSTRAINT CK_accounts_role     CHECK (role IN ('student', 'admin'))
    );
    PRINT N'Đã tạo bảng accounts';
END
ELSE
BEGIN
    PRINT N'Bảng accounts đã tồn tại';
END
GO

-- ============================================================
-- Tài khoản ADMIN mặc định
-- username: admin | password: admin123
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'admin')
BEGIN
    INSERT INTO accounts (username, password, role, full_name, is_active)
    VALUES ('admin', 'admin123', 'admin', N'Quản trị viên', 1);
    PRINT N'Đã tạo tài khoản admin (admin / admin123)';
END
GO

-- ============================================================
-- Tài khoản SINH VIÊN cho dữ liệu demo
-- ============================================================
IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'SV2024001')
BEGIN
    INSERT INTO accounts (username, password, role, student_id, full_name, is_active)
    SELECT 'SV2024001', 'sv001', 'student', id, full_name, 1
    FROM students WHERE student_code = 'SV2024001';
    PRINT N'Đã tạo tài khoản SV2024001 (password: sv001)';
END

IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'SV2024002')
BEGIN
    INSERT INTO accounts (username, password, role, student_id, full_name, is_active)
    SELECT 'SV2024002', 'sv002', 'student', id, full_name, 1
    FROM students WHERE student_code = 'SV2024002';
    PRINT N'Đã tạo tài khoản SV2024002 (password: sv002)';
END

IF NOT EXISTS (SELECT 1 FROM accounts WHERE username = 'SV2024003')
BEGIN
    INSERT INTO accounts (username, password, role, student_id, full_name, is_active)
    SELECT 'SV2024003', 'sv003', 'student', id, full_name, 1
    FROM students WHERE student_code = 'SV2024003';
    PRINT N'Đã tạo tài khoản SV2024003 (password: sv003)';
END
GO

-- Kiểm tra kết quả
SELECT username, role, full_name, is_active, created_at FROM accounts;
PRINT N'=== Migration 03 hoàn thành ===';
GO
