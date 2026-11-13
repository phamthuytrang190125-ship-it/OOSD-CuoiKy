package com.example.baocaosuco.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "KET_QUA_XU_LY")
public class KetQuaXuLy {
    @Id
    @Column(name = "MAKETQUA")
    private String maKetQua;
    
    @Column(name = "MAPHIEU")
    private String maPhieu;
    
    @Column(name = "NGUOICAPNHAT")
    private String nguoiCapNhat;
    
    @Column(name = "NGAYGIOHOANTHANH")
    private LocalDateTime ngayGioHoanThanh;
    
    @Column(name = "PHUONGANXULY")
    private String phuongAnXuLy;

    // Getters & Setters đầy đủ
    public String getMaKetQua() { return maKetQua; }
    public void setMaKetQua(String maKetQua) { this.maKetQua = maKetQua; }

    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }

    public String getNguoiCapNhat() { return nguoiCapNhat; }
    public void setNguoiCapNhat(String nguoiCapNhat) { this.nguoiCapNhat = nguoiCapNhat; }

    public LocalDateTime getNgayGioHoanThanh() { return ngayGioHoanThanh; }
    public void setNgayGioHoanThanh(LocalDateTime ngayGioHoanThanh) { this.ngayGioHoanThanh = ngayGioHoanThanh; }

    public String getPhuongAnXuLy() { return phuongAnXuLy; }
    public void setPhuongAnXuLy(String phuongAnXuLy) { this.phuongAnXuLy = phuongAnXuLy; }
}