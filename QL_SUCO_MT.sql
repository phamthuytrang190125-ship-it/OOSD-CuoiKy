-- I. TẠO CẤU TRÚC BẢNG (DDL)
CREATE TABLE LOAI_SU_CO (
    MaLoai VARCHAR2(20) PRIMARY KEY,
    TenLoai NVARCHAR2(100) NOT NULL,
    MoTa NVARCHAR2(255),
    TrangThai NVARCHAR2(50) NOT NULL
);

CREATE TABLE TO_DOI (
    MaToDoi VARCHAR2(20) PRIMARY KEY,
    TenToDoi NVARCHAR2(100) NOT NULL,
    MaTruongTo VARCHAR2(20),
    SoDienThoai VARCHAR2(20)
);

CREATE TABLE TAI_KHOAN (
    MaTaiKhoan VARCHAR2(20) PRIMARY KEY,
    TenDangNhap VARCHAR2(50) NOT NULL UNIQUE,
    MatKhau VARCHAR2(255) NOT NULL,
    HoTen NVARCHAR2(100) NOT NULL,
    SoDienThoai VARCHAR2(20),
    QuyenHan NVARCHAR2(50) NOT NULL,
    MaToDoi VARCHAR2(20) REFERENCES TO_DOI(MaToDoi),
    TrangThai NVARCHAR2(50) NOT NULL
);
SELECT * FROM TAI_KHOAN;
-- Ràng buộc Khóa ngoại: Tổ trưởng phải là một Tài khoản
ALTER TABLE TO_DOI ADD CONSTRAINT FK_ToDoi_TruongTo FOREIGN KEY (MaTruongTo) REFERENCES TAI_KHOAN(MaTaiKhoan);

CREATE TABLE SU_CO (
    MaSuCo VARCHAR2(20) PRIMARY KEY,
    NgayBaoCao TIMESTAMP NOT NULL,
    HoTenNguoiBao NVARCHAR2(100),
    SoDienThoaiNguoiBao VARCHAR2(20),
    DiaChi NVARCHAR2(255) NOT NULL,
    MaLoai VARCHAR2(20) REFERENCES LOAI_SU_CO(MaLoai),
    MoTaChiTiet NVARCHAR2(500) NOT NULL,
    TrangThai NVARCHAR2(50) NOT NULL,
    LyDoTuChoi NVARCHAR2(255),
    NguoiDuyet VARCHAR2(20) REFERENCES TAI_KHOAN(MaTaiKhoan)
);

CREATE TABLE PHIEU_PHAN_CONG (
    MaPhieu VARCHAR2(20) PRIMARY KEY,
    MaSuCo VARCHAR2(20) REFERENCES SU_CO(MaSuCo),
    NguoiLap VARCHAR2(20) REFERENCES TAI_KHOAN(MaTaiKhoan),
    NgayLap TIMESTAMP NOT NULL,
    MaToDoi VARCHAR2(20) REFERENCES TO_DOI(MaToDoi),
    MucDoUuTien NVARCHAR2(50) NOT NULL,
    ThoiHanXuLy TIMESTAMP NOT NULL,
    GhiChuChiDao NVARCHAR2(500)
);

CREATE TABLE KET_QUA_XU_LY (
    MaKetQua VARCHAR2(20) PRIMARY KEY,
    MaPhieu VARCHAR2(20) REFERENCES PHIEU_PHAN_CONG(MaPhieu),
    NguoiCapNhat VARCHAR2(20) REFERENCES TAI_KHOAN(MaTaiKhoan),
    NgayGioHoanThanh TIMESTAMP NOT NULL,
    PhuongAnXuLy NVARCHAR2(500) NOT NULL
);

CREATE TABLE HINH_ANH (
    MaHinhAnh VARCHAR2(20) PRIMARY KEY,
    DuongDanURL VARCHAR2(500) NOT NULL,
    KichThuoc NUMBER,
    LoaiHinhAnh NVARCHAR2(50) NOT NULL,
    MaThamChieu VARCHAR2(20) NOT NULL
);

CREATE TABLE THONG_BAO (
    MaThongBao VARCHAR2(20) PRIMARY KEY,
    TieuDe NVARCHAR2(100) NOT NULL,
    NoiDung NVARCHAR2(500) NOT NULL,
    NguoiNhan VARCHAR2(20) REFERENCES TAI_KHOAN(MaTaiKhoan),
    ThoiGianTao TIMESTAMP NOT NULL,
    TrangThai NVARCHAR2(50) NOT NULL
);

-- II. THÊM DỮ LIỆU MẪU (DML)
-- 1. LOAI_SU_CO
INSERT INTO LOAI_SU_CO VALUES ('LSC01', N'Rác thải sinh hoạt', N'Xả rác sai quy định, bãi rác tự phát', N'Hoạt động');
INSERT INTO LOAI_SU_CO VALUES ('LSC02', N'Nước thải công nghiệp', N'Xả trộm nước thải chưa qua xử lý', N'Hoạt động');
INSERT INTO LOAI_SU_CO VALUES ('LSC03', N'Ô nhiễm không khí', N'Khói bụi từ nhà máy, đốt rác', N'Hoạt động');
INSERT INTO LOAI_SU_CO VALUES ('LSC04', N'Tiếng ồn', N'Cơ sở sản xuất gây ồn quá mức', N'Hoạt động');

-- 2. TO_DOI (Tạm để NULL Tổ trưởng)
INSERT INTO TO_DOI VALUES ('TD01', N'Đội Phản Ứng Nhanh', NULL, '0901234567');
INSERT INTO TO_DOI VALUES ('TD02', N'Đội Xử Lý Nước Thải', NULL, '0902345678');
INSERT INTO TO_DOI VALUES ('TD03', N'Đội Kiểm Tra Khí Thải', NULL, '0903456789');

-- 3. TAI_KHOAN
INSERT INTO TAI_KHOAN VALUES ('TK_ADMIN', 'admin', 'hashed_pass_1', N'Nguyễn Thái Sơn', '0911111111', N'Quản trị viên', NULL, N'Hoạt động');
INSERT INTO TAI_KHOAN VALUES ('TK_DP01', 'dieuphoi1', 'hashed_pass_2', N'Trần Minh Trí', '0922222222', N'Điều phối viên', NULL, N'Hoạt động');
INSERT INTO TAI_KHOAN VALUES ('TK_NV01', 'nhanvien1', 'hashed_pass_3', N'Lê Diệu Tường', '0933333333', N'Nhân viên', 'TD01', N'Hoạt động');
INSERT INTO TAI_KHOAN VALUES ('TK_NV02', 'nhanvien2', 'hashed_pass_4', N'Phạm Bảo Khang', '0944444444', N'Nhân viên', 'TD02', N'Hoạt động');

-- Cập nhật lại Tổ trưởng cho TO_DOI
UPDATE TO_DOI SET MaTruongTo = 'TK_NV01' WHERE MaToDoi = 'TD01';
UPDATE TO_DOI SET MaTruongTo = 'TK_NV02' WHERE MaToDoi = 'TD02';

-- 4. SU_CO
INSERT INTO SU_CO VALUES ('SC261001', TO_TIMESTAMP('2026-10-01 08:30:00', 'YYYY-MM-DD HH24:MI:SS'), N'Người dân ẩn danh', NULL, N'Khu phố 1, Phường A', 'LSC01', N'Đổ trộm rác thải xây dựng', N'Đã phân công', NULL, 'TK_DP01');
INSERT INTO SU_CO VALUES ('SC261002', TO_TIMESTAMP('2026-10-02 14:15:00', 'YYYY-MM-DD HH24:MI:SS'), N'Nguyễn Văn Phát', '0988888888', N'Khu công nghiệp B', 'LSC02', N'Nước xả có màu đen, mùi hôi', N'Chờ duyệt', NULL, NULL);
INSERT INTO SU_CO VALUES ('SC261003', TO_TIMESTAMP('2026-10-03 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), N'Trần Thị Huệ', '0977777777', N'Hồ nước công viên C', 'LSC01', N'Báo cáo giả mạo', N'Bị từ chối', N'Hình ảnh không xác thực, kiểm tra camera không có', 'TK_DP01');
INSERT INTO SU_CO VALUES ('SC261004', TO_TIMESTAMP('2026-10-04 10:20:00', 'YYYY-MM-DD HH24:MI:SS'), N'Triệu Long', '0966666666', N'Nhà máy thép D', 'LSC03', N'Xả khói đen mịt mù', N'Đã hoàn thành', NULL, 'TK_DP01');

-- 5. PHIEU_PHAN_CONG
INSERT INTO PHIEU_PHAN_CONG VALUES ('PPC001', 'SC261001', 'TK_DP01', TO_TIMESTAMP('2026-10-01 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'TD01', N'Bình thường', TO_TIMESTAMP('2026-10-02 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), N'Xuống dọn dẹp khu vực rác tự phát');
INSERT INTO PHIEU_PHAN_CONG VALUES ('PPC002', 'SC261004', 'TK_DP01', TO_TIMESTAMP('2026-10-04 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'TD03', N'Cao', TO_TIMESTAMP('2026-10-04 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), N'Mang thiết bị đo lường khí thải xuống nhà máy kiểm tra ngay');

-- 6. KET_QUA_XU_LY
INSERT INTO KET_QUA_XU_LY VALUES ('KQ001', 'PPC002', 'TK_NV01', TO_TIMESTAMP('2026-10-04 16:30:00', 'YYYY-MM-DD HH24:MI:SS'), N'Đã lập biên bản xử phạt nhà máy D và yêu cầu khắc phục hệ thống lọc bụi');

-- 7. HINH_ANH
INSERT INTO HINH_ANH VALUES ('IMG001', '/uploads/2026/sc_rac.jpg', 2048, N'Báo cáo', 'SC261001');
INSERT INTO HINH_ANH VALUES ('IMG002', '/uploads/2026/sc_nuoc.jpg', 1024, N'Báo cáo', 'SC261002');
INSERT INTO HINH_ANH VALUES ('IMG003', '/uploads/2026/kq_khoi.jpg', 3072, N'Nghiệm thu', 'KQ001');
INSERT INTO HINH_ANH VALUES ('IMG004', '/uploads/2026/kq_bienban.jpg', 1500, N'Nghiệm thu', 'KQ001');

-- 8. THONG_BAO
INSERT INTO THONG_BAO VALUES ('TB001', N'Có sự cố mới', N'Có báo cáo đổ rác trộm chờ duyệt', 'TK_DP01', TO_TIMESTAMP('2026-10-01 08:35:00', 'YYYY-MM-DD HH24:MI:SS'), N'Đã đọc');
INSERT INTO THONG_BAO VALUES ('TB002', N'Nhiệm vụ mới', N'Tổ đội của bạn vừa nhận phiếu PPC001', 'TK_NV01', TO_TIMESTAMP('2026-10-01 09:05:00', 'YYYY-MM-DD HH24:MI:SS'), N'Chưa đọc');
INSERT INTO THONG_BAO VALUES ('TB003', N'Từ chối sự cố', N'Hồ sơ của bạn đã bị từ chối do không hợp lệ', 'TK_DP01', TO_TIMESTAMP('2026-10-03 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), N'Đã đọc');
INSERT INTO THONG_BAO VALUES ('TB004', N'Hoàn thành', N'Kết quả xử lý KQ001 đã được cập nhật', 'TK_DP01', TO_TIMESTAMP('2026-10-04 16:35:00', 'YYYY-MM-DD HH24:MI:SS'), N'Chưa đọc');

COMMIT;

SELECT * FROM THONG_BAO;


--View xem Chi tiết Sự cố
CREATE OR REPLACE VIEW V_CHI_TIET_SU_CO AS
SELECT 
    s.MaSuCo,
    TO_CHAR(s.NgayBaoCao, 'DD/MM/YYYY HH24:MI') AS NgayBaoCao,
    s.HoTenNguoiBao,
    s.DiaChi,
    l.TenLoai AS LoaiSuCo,
    s.MoTaChiTiet,
    s.TrangThai,
    NVL(t.HoTen, N'Chưa có người duyệt') AS NguoiDuyet
FROM SU_CO s
JOIN LOAI_SU_CO l ON s.MaLoai = l.MaLoai
LEFT JOIN TAI_KHOAN t ON s.NguoiDuyet = t.MaTaiKhoan;

-- Test View 1: Xem toàn bộ danh sách chi tiết sự cố
SELECT * FROM V_CHI_TIET_SU_CO;

--View xem Tiến độ Phân công
CREATE OR REPLACE VIEW V_TIEN_DO_PHAN_CONG AS
SELECT 
    p.MaPhieu,
    s.MaSuCo,
    l.TenLoai,
    s.DiaChi,
    td.TenToDoi AS DoiPhuTrach,
    TO_CHAR(p.ThoiHanXuLy, 'DD/MM/YYYY HH24:MI') AS HanChot,
    p.MucDoUuTien,
    s.TrangThai AS TrangThaiXuLy
FROM PHIEU_PHAN_CONG p
JOIN SU_CO s ON p.MaSuCo = s.MaSuCo
JOIN LOAI_SU_CO l ON s.MaLoai = l.MaLoai
JOIN TO_DOI td ON p.MaToDoi = td.MaToDoi;

-- Test View 2: Xem tiến độ của các phiếu phân công
SELECT * FROM V_TIEN_DO_PHAN_CONG;

-- Tạo Trigger tự động cập nhật trạng thái Sự cố
CREATE OR REPLACE TRIGGER TRG_AUTO_CAPNHAT_TRANGTHAI
AFTER INSERT ON KET_QUA_XU_LY
FOR EACH ROW
DECLARE
    v_MaSuCo VARCHAR2(20);
BEGIN
    -- 1. Tìm Mã sự cố tương ứng từ bảng PHIEU_PHAN_CONG dựa vào Mã phiếu vừa nghiệm thu
    SELECT MaSuCo INTO v_MaSuCo
    FROM PHIEU_PHAN_CONG
    WHERE MaPhieu = :NEW.MaPhieu;

    -- 2. Tự động cập nhật trạng thái sự cố đó thành 'Đã hoàn thành'
    UPDATE SU_CO
    SET TrangThai = N'Đã hoàn thành'
    WHERE MaSuCo = v_MaSuCo;
    
    DBMS_OUTPUT.PUT_LINE('Đã tự động cập nhật trạng thái Sự cố: ' || v_MaSuCo || ' thành Đã hoàn thành!');
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Không tìm thấy Sự cố tương ứng cho Phiếu phân công này.');
END;
/

-- 1. Thêm một kết quả xử lý mới cho Phiếu PPC001
INSERT INTO KET_QUA_XU_LY (MaKetQua, MaPhieu, NguoiCapNhat, NgayGioHoanThanh, PhuongAnXuLy)
VALUES ('KQ002', 'PPC001', 'TK_NV01', SYSTIMESTAMP, N'Đã dọn sạch rác tự phát');
SELECT * FROM KET_QUA_XU_LY;

-- 2. Commit dữ liệu
COMMIT;

-- 3. Kiểm tra lại bảng SU_CO, bạn sẽ thấy trạng thái của SC261001 tự động nhảy sang "Đã hoàn thành"
SELECT MaSuCo, MoTaChiTiet, TrangThai 
FROM SU_CO 
WHERE MaSuCo = 'SC261001';

--kiểm tra báo vừa gửi 
SELECT * FROM SU_CO ORDER BY NGAYBAOCAO DESC;

--kiểm tra báo theo thời gian
SELECT MASUCO, HOTENNGUOIBAO, DIACHI, MALOAI, TRANGTHAI, NGAYBAOCAO 
FROM SU_CO 
ORDER BY NGAYBAOCAO DESC;


--Kiểm tra trạng thái 
SELECT MASUCO, HOTENNGUOIBAO, DIACHI, TRANGTHAI, NGAYBAOCAO 
FROM SU_CO 
WHERE TRANGTHAI = 'Đang xử lý'
ORDER BY NGAYBAOCAO DESC;


SELECT * FROM PHIEU_PHAN_CONG;

--Kiểm tra bảng Báo cáo sự cố 
SELECT * FROM SU_CO ORDER BY NgayBaoCao DESC;

--Kiểm tra bảng Lịch sử phân công 
SELECT * FROM PHIEU_PHAN_CONG ORDER BY NgayLap DESC;

--xem tổng quát toàn bộ chu trình 
SELECT 
    s.MaSuCo, 
    s.DiaChi, 
    s.TrangThai, 
    p.MaPhieu, 
    p.MaToDoi, 
    p.NguoiLap 
FROM SU_CO s
LEFT JOIN PHIEU_PHAN_CONG p ON s.MaSuCo = p.MaSuCo
ORDER BY s.NgayBaoCao DESC;


-- 1. Thêm tài khoản nhân viên số 3 vào bảng TAI_KHOAN
INSERT INTO TAI_KHOAN VALUES ('TK_NV03', 'nhanvien3', 'hashed_pass_5', N'Nguyễn Trọng Khoa', '0955555555', N'Nhân viên', 'TD03', N'Hoạt động');
-- 2. Cập nhật nhân viên này làm Tổ trưởng của đội TD03
UPDATE TO_DOI SET MaTruongTo = 'TK_NV03' WHERE MaToDoi = 'TD03';
COMMIT;


-- 1. Thêm thành viên cho Đội Phản Ứng Nhanh (TD01)
INSERT INTO TAI_KHOAN VALUES ('TK_NV04', 'nhanvien4', 'hashed_pass_6', N'Lê Minh Tuấn', '0912121212', N'Nhân viên', 'TD01', N'Hoạt động');
INSERT INTO TAI_KHOAN VALUES ('TK_NV05', 'nhanvien5', 'hashed_pass_7', N'Phạm Thu Thủy', '0934343434', N'Nhân viên', 'TD01', N'Hoạt động');

-- 2. Thêm thành viên cho Đội Xử Lý Nước Thải (TD02)
INSERT INTO TAI_KHOAN VALUES ('TK_NV06', 'nhanvien6', 'hashed_pass_8', N'Trần Quốc Bảo', '0956565656', N'Nhân viên', 'TD02', N'Hoạt động');
INSERT INTO TAI_KHOAN VALUES ('TK_NV07', 'nhanvien7', 'hashed_pass_9', N'Nguyễn Thị Lan', '0978787878', N'Nhân viên', 'TD02', N'Hoạt động');

-- 3. Thêm thành viên cho Đội Kiểm Tra Khí Thải (TD03)
INSERT INTO TAI_KHOAN VALUES ('TK_NV08', 'nhanvien8', 'hashed_pass_10', N'Hoàng Anh Thái', '0990909090', N'Nhân viên', 'TD03', N'Hoạt động');
INSERT INTO TAI_KHOAN VALUES ('TK_NV09', 'nhanvien9', 'hashed_pass_11', N'Đặng Phương Nam', '0923232323', N'Nhân viên', 'TD03', N'Hoạt động');

-- Lưu lại toàn bộ thay đổi
COMMIT;

-- 1. Nhóm Quản lý (Admin & Điều phối)
UPDATE TAI_KHOAN SET HoTen = N'Phạm Thị Thuỳ Trang' WHERE MaTaiKhoan = 'TK_ADMIN';
UPDATE TAI_KHOAN SET HoTen = N'Từ Chấn Hiên' WHERE MaTaiKhoan = 'TK_DP01';

-- 2. Đội Phản Ứng Nhanh (TD01)
UPDATE TAI_KHOAN SET HoTen = N'Tiêu Thu Thuỷ' WHERE MaTaiKhoan = 'TK_NV01';
UPDATE TAI_KHOAN SET HoTen = N'Đường Phương' WHERE MaTaiKhoan = 'TK_NV04';
UPDATE TAI_KHOAN SET HoTen = N'Đường Nhu' WHERE MaTaiKhoan = 'TK_NV05';

-- 3. Đội Xử Lý Nước Thải (TD02)
UPDATE TAI_KHOAN SET HoTen = N'Liễu Tuỳ Phong' WHERE MaTaiKhoan = 'TK_NV02';
UPDATE TAI_KHOAN SET HoTen = N'Tống Minh Châu' WHERE MaTaiKhoan = 'TK_NV06';
UPDATE TAI_KHOAN SET HoTen = N'Tiêu Tuyết Ngư' WHERE MaTaiKhoan = 'TK_NV07';

-- 4. Đội Kiểm Tra Khí Thải (TD03)
UPDATE TAI_KHOAN SET HoTen = N'Lý Trầm Chu' WHERE MaTaiKhoan = 'TK_NV03';
UPDATE TAI_KHOAN SET HoTen = N'Triệu Sư Dung' WHERE MaTaiKhoan = 'TK_NV08';
UPDATE TAI_KHOAN SET HoTen = N'Yến Cuồng Đồ' WHERE MaTaiKhoan = 'TK_NV09';

-- BẮT BUỘC CÓ DÒNG NÀY ĐỂ LƯU THAY ĐỔI XUỐNG Ổ CỨNG
COMMIT;

--ai đang làm Tổ trưởng của đội nào
SELECT 
    td.MaToDoi AS "Mã Đội",
    td.TenToDoi AS "Tên Tổ Đội",
    td.MaTruongTo AS "Mã TK Tổ Trưởng",
    tk.HoTen AS "Họ Tên Tổ Trưởng",
    td.SoDienThoai AS "SĐT Hotline Đội"
FROM TO_DOI td
LEFT JOIN TAI_KHOAN tk ON td.MaTruongTo = tk.MaTaiKhoan
ORDER BY td.MaToDoi;

UPDATE PHIEU_PHAN_CONG SET MucDoUuTien = N'Cao' WHERE MaPhieu = 'PPC38036';
UPDATE PHIEU_PHAN_CONG SET MucDoUuTien = N'Thấp' WHERE MaPhieu = 'PPC6665';
COMMIT;

SELECT TenDangNhap, HoTen FROM TAI_KHOAN WHERE TenDangNhap = 'dieuphoi1';

-- Cập nhật lại phân loại và mã tham chiếu cho đúng thực tế
UPDATE HINH_ANH SET LOAIHINHANH = N'Báo cáo', MATHAMCHIEU = 'SC261001' WHERE MAHINHANH = 'IMG001';
UPDATE HINH_ANH SET LOAIHINHANH = N'Nghiệm thu', MATHAMCHIEU = 'KQ002' WHERE MAHINHANH = 'IMG003';
COMMIT;


SELECT * FROM PHIEU_PHAN_CONG WHERE MASUCO = 'SC-D9808';

-- 1. Chèn kết quả xử lý ứng với mã phiếu PPC20820
INSERT INTO KET_QUA_XU_LY (MAKETQUA, MAPHIEU, NGUOICAPNHAT, NGAYGIOHOANTHANH, PHUONGANXULY) 
VALUES ('KQ888', 'PPC20820', 'TK_NV01', SYSDATE, N'Đội hiện trường đã xử lý dứt điểm sự cố và dọn dẹp sạch sẽ khu vực.');

-- 2. Chèn ảnh nghiệm thu liên kết với mã kết quả KQ888
INSERT INTO HINH_ANH (MAHINHANH, DUONGDANURL, KICHTHUOC, LOAIHINHANH, MATHAMCHIEU) 
VALUES ('IMG888', '/uploads/2026/kq_khoi.jpg', 3072, N'Nghiệm thu', 'KQ888');

COMMIT;

DELETE FROM KET_QUA_XU_LY WHERE MAPHIEU = 'PPC20820';
COMMIT;

-- 1. Chèn kết quả xử lý kết nối chuẩn với mã phiếu PPC20820
INSERT INTO KET_QUA_XU_LY (MAKETQUA, MAPHIEU, NGUOICAPNHAT, NGAYGIOHOANTHANH, PHUONGANXULY) 
VALUES ('KQ999', 'PPC20820', 'TK_NV01', SYSDATE, N'Đội hiện trường đã xử lý dứt điểm sự cố và vệ sinh khu vực an toàn.');

-- 2. Chèn ảnh nghiệm thu có mã tham chiếu trỏ đúng vào mã kết quả KQ999
INSERT INTO HINH_ANH (MAHINHANH, DUONGDANURL, KICHTHUOC, LOAIHINHANH, MATHAMCHIEU) 
VALUES ('IMG999', '/uploads/2026/kq_khoi.jpg', 3072, N'Nghiệm thu', 'KQ999');

COMMIT;