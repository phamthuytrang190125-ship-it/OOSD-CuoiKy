package com.example.baocaosuco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.baocaosuco.entity.HinhAnh;
import com.example.baocaosuco.entity.KetQuaXuLy;
import com.example.baocaosuco.entity.PhieuPhanCong;
import com.example.baocaosuco.entity.SuCo;
import com.example.baocaosuco.entity.TaiKhoan;
import com.example.baocaosuco.entity.ThongBao;

import com.example.baocaosuco.repository.HinhAnhRepository;
import com.example.baocaosuco.repository.KetQuaXuLyRepository;
import com.example.baocaosuco.repository.PhieuPhanCongRepository;
import com.example.baocaosuco.repository.SuCoRepository;
import com.example.baocaosuco.repository.TaiKhoanRepository;
import com.example.baocaosuco.repository.ThongBaoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class SuCoController {

    @Autowired
    private SuCoRepository suCoRepository;
    
    @Autowired
    private PhieuPhanCongRepository phieuPhanCongRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private KetQuaXuLyRepository ketQuaXuLyRepository;

    @Autowired
    private HinhAnhRepository hinhAnhRepository;

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    // 1. Hiển thị Trang chủ (Landing Page)
    @GetMapping("/")
    public String showIndex(Model model) {
        List<SuCo> listSuCo = suCoRepository.findAllByOrderByNgayBaoCaoDesc();
        listSuCo.removeIf(sc -> sc.getMaLoai() == null || sc.getMaLoai().equals("Khác") || sc.getMaLoai().equals("LSC05"));
        
        model.addAttribute("listSuCo", listSuCo);
        return "index";
    }

    // 2. Hiển thị Form lập báo cáo sự cố
    @GetMapping("/baocao")
    public String showBaoCaoForm(Model model) {
        model.addAttribute("suCo", new SuCo());
        return "baocao";
    }

    // 3. Xử lý lưu dữ liệu vào Oracle khi người dân bấm Gửi báo cáo (Đã tích hợp upload ảnh)
    @PostMapping("/submit-bao-cao")
    public String submitBaoCao(SuCo suCo, 
                               @RequestParam(name = "fileAnh", required = false) org.springframework.web.multipart.MultipartFile fileAnh, 
                               Model model) {
        
        String maSuCoMoi = "SC-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        suCo.setMaSuCo(maSuCoMoi);
        suCo.setNgayBaoCao(LocalDateTime.now());
        suCo.setTrangThai("Chờ duyệt");
        
        // Lưu thông tin sự cố vào cơ sở dữ liệu
        suCoRepository.save(suCo);

        // XỬ LÝ LƯU HÌNH ẢNH ĐÍNH KÈM VÀO BẢNG HINH_ANH
        try {
            String urlAnh = "/uploads/2026/sc_rac.jpg"; // Ảnh mặc định nếu người dân không chọn file
            long kichThuoc = 2048L;

            if (fileAnh != null && !fileAnh.isEmpty()) {
                String fileName = fileAnh.getOriginalFilename();
                String uploadDir = "src/main/resources/static/uploads/2026/";
                
                java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);
                if (!java.nio.file.Files.exists(uploadPath)) {
                    java.nio.file.Files.createDirectories(uploadPath);
                }
                
                // Lưu file thực tế vào thư mục static của dự án
                fileAnh.transferTo(uploadPath.resolve(fileName).toFile());
                urlAnh = "/uploads/2026/" + fileName;
                kichThuoc = fileAnh.getSize() / 1024; // Đổi sang đơn vị KB
            }

            // Ghi dữ liệu vào bảng HINH_ANH
            HinhAnh img = new HinhAnh();
            img.setMaHinhAnh("IMG" + System.currentTimeMillis() % 100000);
            img.setDuongDanURL(urlAnh);
            img.setKichThuoc(kichThuoc);
            img.setLoaiHinhAnh("Báo cáo");
            img.setMaThamChieu(maSuCoMoi);
            hinhAnhRepository.save(img);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // TỰ ĐỘNG TẠO THÔNG BÁO MỚI CHO ĐIỀU PHỐI VIÊN (BẢNG THONG_BAO)
        ThongBao tb = new ThongBao();
        tb.setMaThongBao("TB" + System.currentTimeMillis() % 100000);
        tb.setTieuDe("Báo cáo sự cố mới");
        tb.setNoiDung("Có phản ánh mới mã " + maSuCoMoi + " tại: " + suCo.getDiaChi());
        tb.setNguoiNhan("TK_DP01");
        tb.setThoiGianTao(LocalDateTime.now());
        tb.setTrangThai("Chưa đọc");
        thongBaoRepository.save(tb);
        
        model.addAttribute("message", "Gửi báo cáo thành công! Mã tra cứu của bạn là: " + suCo.getMaSuCo());
        model.addAttribute("suCo", new SuCo());
        
        return "baocao"; 
    }

    // 4. Trang Đăng nhập nội bộ
    @GetMapping("/login")
    public String showLogin() {
        return "login"; 
    }

    // 5. Xử lý phân quyền & Truy vấn dữ liệu thực từ Oracle cho trang Admin/Điều phối
    @GetMapping("/admin")
    public String showAdmin(@RequestParam(name = "username", defaultValue = "admin") String username, Model model) {
        
        if ("admin".equals(username)) {
            return "redirect:/taikhoan";
        } 
        
        if ("dieuphoi1".equals(username)) {
            model.addAttribute("hoTen", "Từ Chấn Hiên");
            model.addAttribute("quyenHan", "Điều phối viên");
            model.addAttribute("username", username);
            
            List<SuCo> listSuCo = suCoRepository.findAllByOrderByNgayBaoCaoDesc();
            listSuCo.removeIf(sc -> sc.getMaLoai() == null || sc.getMaLoai().equals("Khác") || sc.getMaLoai().equals("LSC05"));
            model.addAttribute("listSuCo", listSuCo);

            // LẤY DANH SÁCH THÔNG BÁO CỦA ĐIỀU PHỐI VIÊN (TK_DP01)
            List<ThongBao> listThongBao = thongBaoRepository.findByNguoiNhanOrderByThoiGianTaoDesc("TK_DP01");
            model.addAttribute("listThongBao", listThongBao);

            return "admin";
        } 
        
        if ("nhanvien1".equals(username) || "nhanvien2".equals(username) || "nhanvien3".equals(username)) {
            return "redirect:/nhiemvu?username=" + username;
        }
        
        return "redirect:/login";
    }

    // 6. Trang nhiệm vụ dành riêng cho Nhân viên hiện trường
    @GetMapping("/nhiemvu")
    public String showNhiemVu(@RequestParam(name = "username", defaultValue = "nhanvien1") String username, Model model) {
        String hoTen = "Tiêu Thu Thuỷ";
        String quyenHan = "Nhân viên hiện trường (Đội Phản Ứng Nhanh)";
        String maToDoi = "TD01"; 
        String maTaiKhoan = "TK_NV01";

        if ("nhanvien2".equals(username)) {
            hoTen = "Liễu Tuỳ Phong";
            quyenHan = "Nhân viên hiện trường (Đội Xử Lý Nước Thải)";
            maToDoi = "TD02";
            maTaiKhoan = "TK_NV02";
        } else if ("nhanvien3".equals(username)) {
            hoTen = "Lý Trầm Chu";
            quyenHan = "Nhân viên hiện trường (Đội Kiểm Tra Khí Thải)";
            maToDoi = "TD03";
            maTaiKhoan = "TK_NV03";
        }

        List<PhieuPhanCong> listPhieu = phieuPhanCongRepository.findByMaToDoiOrderByNgayLapDesc(maToDoi);

        // LẤY DANH SÁCH THÔNG BÁO CỦA NHÂN VIÊN TƯƠNG ỨNG
        List<ThongBao> listThongBao = thongBaoRepository.findByNguoiNhanOrderByThoiGianTaoDesc(maTaiKhoan);

        model.addAttribute("hoTen", hoTen);
        model.addAttribute("quyenHan", quyenHan);
        model.addAttribute("username", username);
        model.addAttribute("listPhieu", listPhieu);
        model.addAttribute("listThongBao", listThongBao);
        return "nhiemvu";
    }

    // 7. Trang Tra cứu trạng thái sự cố (Tích hợp HINH_ANH và KET_QUA_XU_LY)
    @GetMapping("/tracuu")
    public String showTraCuu(@RequestParam(name = "maSuCo", required = false) String maSuCo, Model model) {
        if (maSuCo != null && !maSuCo.trim().isEmpty()) {
            String maTimKiem = maSuCo.trim().toUpperCase();
            SuCo suCo = suCoRepository.findById(maTimKiem).orElse(null);
            
            if (suCo != null) {
                model.addAttribute("suCoTimThay", suCo);

                // 1. Lấy hình ảnh hiện trường đính kèm của sự cố này (BẢNG HINH_ANH)
                List<HinhAnh> listHinhAnh = hinhAnhRepository.findByMaThamChieu(maTimKiem);
                model.addAttribute("listHinhAnh", listHinhAnh);

                // 2. Tìm kết quả xử lý nghiệm thu nếu có (BẢNG KET_QUA_XU_LY)
                List<PhieuPhanCong> allPhieu = phieuPhanCongRepository.findAll();
                for (PhieuPhanCong p : allPhieu) {
                    // Thêm trim() và equalsIgnoreCase() để so sánh chuẩn xác tuyệt đối
                    if (p.getMaSuCo() != null && p.getMaSuCo().trim().equalsIgnoreCase(maTimKiem)) {
                        KetQuaXuLy kq = ketQuaXuLyRepository.findByMaPhieu(p.getMaPhieu());
                        if (kq != null) {
                            model.addAttribute("ketQuaXuLy", kq);
                            // Lấy thêm hình ảnh nghiệm thu dựa theo mã kết quả (maKetQua)
                            List<HinhAnh> listAnhNghiemThu = hinhAnhRepository.findByMaThamChieu(kq.getMaKetQua());
                            model.addAttribute("listAnhNghiemThu", listAnhNghiemThu);
                        }
                        break; // Đưa break vào trong if để dừng đúng lúc khi tìm thấy phiếu!
                    }
                }
            } else {
                model.addAttribute("errorMsg", "Không tìm thấy hồ sơ sự cố nào với mã số: " + maSuCo);
            }
        }
        model.addAttribute("maSuCoNhap", maSuCo); 
        return "tracuu"; 
    }

    // 8. Trang Báo cáo thống kê
    @GetMapping("/thongke")
    public String showThongKe(@RequestParam(name = "username", defaultValue = "dieuphoi1") String username, Model model) {
        String hoTen = "Từ Chấn Hiên";
        String quyenHan = "Điều phối viên";

        TaiKhoan tk = taiKhoanRepository.findByTenDangNhap(username);
        if (tk != null) {
            hoTen = tk.getHoTen();
            quyenHan = tk.getQuyenHan();
        }

        model.addAttribute("hoTen", hoTen);
        model.addAttribute("quyenHan", quyenHan);
        model.addAttribute("username", username);

        List<SuCo> listSuCo = suCoRepository.findAll();
        listSuCo.removeIf(sc -> sc.getMaLoai() == null || sc.getMaLoai().equals("Khác") || sc.getMaLoai().equals("LSC05"));

        long tongSo = listSuCo.size();
        long dangXuLy = listSuCo.stream().filter(sc -> "Đang xử lý".equals(sc.getTrangThai())).count();
        long daHoanThanh = listSuCo.stream().filter(sc -> "Đã hoàn thành".equals(sc.getTrangThai())).count();
        long choDuyet = listSuCo.stream().filter(sc -> "Chờ duyệt".equals(sc.getTrangThai())).count();
        long biTuChoi = listSuCo.stream().filter(sc -> "Bị từ chối".equals(sc.getTrangThai())).count();

        model.addAttribute("tongSo", tongSo);
        model.addAttribute("dangXuLy", dangXuLy);
        model.addAttribute("daHoanThanh", daHoanThanh);
        model.addAttribute("choDuyet", choDuyet);
        model.addAttribute("biTuChoi", biTuChoi);

        return "thongke";
    }

    // 9. Trang Quản lý danh mục
    @GetMapping("/danhmuc")
    public String showDanhMuc(@RequestParam(name = "username", defaultValue = "admin") String username, Model model) {
        String hoTen = "Phạm Thị Thuỳ Trang";
        String quyenHan = "Quản trị viên";

        if ("dieuphoi1".equals(username)) {
            hoTen = "Từ Chấn Hiên";
            quyenHan = "Điều phối viên";
        }

        model.addAttribute("hoTen", hoTen);
        model.addAttribute("quyenHan", quyenHan);
        model.addAttribute("username", username);
        return "danhmuc";
    }

    // 10. Trang Quản lý tài khoản nội bộ
    @GetMapping("/taikhoan")
    public String showTaiKhoan(@RequestParam(name = "username", defaultValue = "admin") String username, Model model) {
        String hoTen = "Phạm Thị Thuỳ Trang";
        String quyenHan = "Quản trị viên";

        if ("dieuphoi1".equals(username)) {
            hoTen = "Từ Chấn Hiên";
            quyenHan = "Điều phối viên";
        }

        model.addAttribute("hoTen", hoTen);
        model.addAttribute("quyenHan", quyenHan);
        model.addAttribute("username", username);

        List<TaiKhoan> listTaiKhoan = taiKhoanRepository.findAll();
        model.addAttribute("listTaiKhoan", listTaiKhoan);

        return "taikhoan";
    }

    // 11. Trang Hồ sơ cá nhân
    @GetMapping("/hoso")
    public String showHoSo(@RequestParam(name = "username", defaultValue = "dieuphoi1") String username, Model model) {
        String hoTen = "Từ Chấn Hiên";
        String quyenHan = "Điều phối viên";
        String email = "dieuphoi1@envreport.vn";
        String sdt = "0909.123.456";

        if ("nhanvien1".equals(username)) {
            hoTen = "Tiêu Thu Thuỷ";
            quyenHan = "Nhân viên hiện trường (Đội Phản Ứng Nhanh)";
            email = "nhanvien1@envreport.vn";
            sdt = "0933.333.333";
        } else if ("nhanvien2".equals(username)) {
            hoTen = "Liễu Tuỳ Phong";
            quyenHan = "Nhân viên hiện trường (Đội Xử Lý Nước Thải)";
            email = "nhanvien2@envreport.vn";
            sdt = "0944.444.444";
        } else if ("nhanvien3".equals(username)) {
            hoTen = "Lý Trầm Chu";
            quyenHan = "Nhân viên hiện trường (Đội Kiểm Tra Khí Thải)";
            email = "nhanvien3@envreport.vn";
            sdt = "0955.555.555";
        } else if ("admin".equals(username)) {
            hoTen = "Phạm Thị Thuỳ Trang";
            quyenHan = "Quản trị viên";
            email = "admin@envreport.vn";
            sdt = "0911.111.111";
        }

        model.addAttribute("hoTen", hoTen);
        model.addAttribute("quyenHan", quyenHan);
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        model.addAttribute("sdt", sdt);
        
        return "hoso";
    }

    // 12. Trang Quản lý tổ đội phụ trách
    @GetMapping("/todoi")
    public String showToDoi(@RequestParam(name = "username", defaultValue = "admin") String username, Model model) {
        String hoTen = "Phạm Thị Thuỳ Trang";
        String quyenHan = "Quản trị viên";

        if ("dieuphoi1".equals(username)) {
            hoTen = "Từ Chấn Hiên";
            quyenHan = "Điều phối viên";
        }

        model.addAttribute("hoTen", hoTen);
        model.addAttribute("quyenHan", quyenHan);
        model.addAttribute("username", username);
        return "todoi";
    }

    // 13. Xử lý khi Điều phối viên chọn đội, chọn mức độ và bấm "Phân công"
    @PostMapping("/admin/duyet")
    public String duyetSuCo(@RequestParam("maSuCo") String maSuCo, 
                            @RequestParam("maToDoi") String maToDoi,
                            @RequestParam("mucDoUuTien") String mucDoUuTien,
                            @RequestParam(name="username", defaultValue="dieuphoi1") String username) {
        
        SuCo suCo = suCoRepository.findById(maSuCo).orElse(null);
        if (suCo != null) {
            suCo.setTrangThai("Đang xử lý");
            suCo.setNguoiDuyet("TK_DP01");
            suCoRepository.save(suCo);
            
            String maTaiKhoan = "TK_DP01"; 
            String maPhieuMoi = "PPC" + System.currentTimeMillis() % 100000;

            PhieuPhanCong ppc = new PhieuPhanCong();
            ppc.setMaPhieu(maPhieuMoi); 
            ppc.setMaSuCo(maSuCo);
            ppc.setMaToDoi(maToDoi);
            ppc.setNguoiLap(maTaiKhoan); 
            ppc.setNgayLap(LocalDateTime.now());
            ppc.setMucDoUuTien(mucDoUuTien); 
            ppc.setThoiHanXuLy(LocalDateTime.now().plusDays(3)); 
            ppc.setGhiChuChiDao("Tiếp nhận và xử lý kịp thời hiện trường.");
            phieuPhanCongRepository.save(ppc);

            // TỰ ĐỘNG TẠO THÔNG BÁO GIAO VIỆC CHO TỔ TRƯỞNG ĐỘI TƯƠNG ỨNG (BẢNG THONG_BAO)
            String maTruongTo = "TK_NV01";
            if ("TD02".equals(maToDoi)) maTruongTo = "TK_NV02";
            else if ("TD03".equals(maToDoi)) maTruongTo = "TK_NV03";

            ThongBao tb = new ThongBao();
            tb.setMaThongBao("TB" + System.currentTimeMillis() % 100000);
            tb.setTieuDe("Nhiệm vụ mới");
            tb.setNoiDung("Tổ đội của bạn vừa nhận phiếu phân công " + maPhieuMoi + " (Mức độ: " + mucDoUuTien + ")");
            tb.setNguoiNhan(maTruongTo);
            tb.setThoiGianTao(LocalDateTime.now());
            tb.setTrangThai("Chưa đọc");
            thongBaoRepository.save(tb);
        }
        return "redirect:/admin?username=" + username;
    }

    // 14. Xử lý khi Điều phối viên bấm nút "Từ chối"
    @PostMapping("/admin/tuchoi")
    public String tuChoiSuCo(@RequestParam("maSuCo") String maSuCo, 
                             @RequestParam(name="username", defaultValue="dieuphoi1") String username) {
        
        SuCo suCo = suCoRepository.findById(maSuCo).orElse(null);
        if (suCo != null) {
            suCo.setTrangThai("Bị từ chối"); 
            suCo.setNguoiDuyet("TK_DP01");
            suCo.setLyDoTuChoi("Thông tin phản ánh không xác thực hoặc trùng lặp.");
            suCoRepository.save(suCo);
        }
        
        return "redirect:/admin?username=" + username;
    }

    // 15. Xử lý khi nhân viên bấm "Đánh dấu đã xử lý" hiện trường (Tích hợp KET_QUA_XU_LY, THONG_BAO & HINH_ANH NGHIỆM THU)
    @PostMapping("/nhanvien/capnhat")
    public String capNhatKetQua(@RequestParam("maSuCo") String maSuCo,
                                @RequestParam(name="username", defaultValue="nhanvien1") String username) {
        
        SuCo suCo = suCoRepository.findById(maSuCo).orElse(null);
        if (suCo != null) {
            suCo.setTrangThai("Đã hoàn thành");
            suCoRepository.save(suCo);

            // Xác định mã tài khoản nhân viên cập nhật
            String maNV = "TK_NV01";
            if ("nhanvien2".equals(username)) maNV = "TK_NV02";
            else if ("nhanvien3".equals(username)) maNV = "TK_NV03";

            // Tìm mã phiếu phân công tương ứng với mã sự cố
            String maPhieu = "PPC001";
            List<PhieuPhanCong> allPhieu = phieuPhanCongRepository.findAll();
            for (PhieuPhanCong p : allPhieu) {
                if (maSuCo.equals(p.getMaSuCo())) {
                    maPhieu = p.getMaPhieu();
                    break;
                }
            }

            // 1. LƯU BIÊN BẢN NGHIỆM THU VÀO BẢNG KET_QUA_XU_LY
            String maKQ = "KQ" + System.currentTimeMillis() % 100000;
            KetQuaXuLy kq = new KetQuaXuLy();
            kq.setMaKetQua(maKQ);
            kq.setMaPhieu(maPhieu);
            kq.setNguoiCapNhat(maNV);
            kq.setNgayGioHoanThanh(LocalDateTime.now());
            kq.setPhuongAnXuLy("Đội hiện trường đã xử lý dứt điểm sự cố và vệ sinh khu vực an toàn.");
            ketQuaXuLyRepository.save(kq);

            // 2. TỰ ĐỘNG GẮN HÌNH ẢNH NGHIỆM THU VÀO BẢNG HINH_ANH (Gắn với mã kết quả maKQ)
            try {
                HinhAnh imgNT = new HinhAnh();
                imgNT.setMaHinhAnh("IMG" + (System.currentTimeMillis() + 1) % 100000);
                imgNT.setDuongDanURL("/uploads/2026/kq_khoi.jpg"); // Đường dẫn ảnh nghiệm thu sạch sẽ
                imgNT.setKichThuoc(3072L);
                imgNT.setLoaiHinhAnh("Nghiệm thu");
                imgNT.setMaThamChieu(maKQ); // Liên kết với mã kết quả xử lý
                hinhAnhRepository.save(imgNT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 3. TẠO THÔNG BÁO BÁO CÁO HOÀN THÀNH VỀ CHO ĐIỀU PHỐI VIÊN (BẢNG THONG_BAO)
            ThongBao tb = new ThongBao();
            tb.setMaThongBao("TB" + System.currentTimeMillis() % 100000);
            tb.setTieuDe("Hoàn thành xử lý");
            tb.setNoiDung("Sự cố " + maSuCo + " (Phiếu " + maPhieu + ") đã được nghiệm thu với mã " + maKQ);
            tb.setNguoiNhan("TK_DP01");
            tb.setThoiGianTao(LocalDateTime.now());
            tb.setTrangThai("Chưa đọc");
            thongBaoRepository.save(tb);
        }
        
        return "redirect:/nhiemvu?username=" + username;
    }
}