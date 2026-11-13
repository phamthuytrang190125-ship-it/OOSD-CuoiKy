package com.example.baocaosuco.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PHIEU_PHAN_CONG")
public class PhieuPhanCong {

    @Id
    @Column(name = "MAPHIEU")
    private String maPhieu;

    @Column(name = "MASUCO")
    private String maSuCo;

    @Column(name = "NGUOILAP")
    private String nguoiLap;

    @Column(name = "NGAYLAP")
    private LocalDateTime ngayLap;

    @Column(name = "MATODOI")
    private String maToDoi;

    @Column(name = "MUCDOUUTIEN")
    private String mucDoUuTien;

    @Column(name = "THOIHANXULY")
    private LocalDateTime thoiHanXuLy;

    @Column(name = "GHICHUCHIDAO")
    private String ghiChuChiDao;

    // Liên kết dữ liệu với bảng SU_CO để lấy địa chỉ hiển thị lên giao diện nhân viên
    @ManyToOne
    @JoinColumn(name = "MASUCO", referencedColumnName = "MASUCO", insertable = false, updatable = false)
    private SuCo suCo;

    // Getters và Setters
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }

    public String getMaSuCo() { return maSuCo; }
    public void setMaSuCo(String maSuCo) { this.maSuCo = maSuCo; }

    public String getNguoiLap() { return nguoiLap; }
    public void setNguoiLap(String nguoiLap) { this.nguoiLap = nguoiLap; }

    public LocalDateTime getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDateTime ngayLap) { this.ngayLap = ngayLap; }

    public String getMaToDoi() { return maToDoi; }
    public void setMaToDoi(String maToDoi) { this.maToDoi = maToDoi; }

    public String getMucDoUuTien() { return mucDoUuTien; }
    public void setMucDoUuTien(String mucDoUuTien) { this.mucDoUuTien = mucDoUuTien; }

    public LocalDateTime getThoiHanXuLy() { return thoiHanXuLy; }
    public void setThoiHanXuLy(LocalDateTime thoiHanXuLy) { this.thoiHanXuLy = thoiHanXuLy; }

    public String getGhiChuChiDao() { return ghiChuChiDao; }
    public void setGhiChuChiDao(String ghiChuChiDao) { this.ghiChuChiDao = ghiChuChiDao; }

    public SuCo getSuCo() { return suCo; }
    public void setSuCo(SuCo suCo) { this.suCo = suCo; }
}