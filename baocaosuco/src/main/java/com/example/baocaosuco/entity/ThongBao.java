package com.example.baocaosuco.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "THONG_BAO")
public class ThongBao {
    @Id
    @Column(name = "MATHONGBAO")
    private String maThongBao;

    @Column(name = "TIEUDE")
    private String tieuDe;

    @Column(name = "NOIDUNG")
    private String noiDung;

    @Column(name = "NGUOINHAN")
    private String nguoiNhan;

    @Column(name = "THOIGIANTAO")
    private LocalDateTime thoiGianTao;

    @Column(name = "TRANGTHAI")
    private String trangThai;

    // Getters & Setters
    public String getMaThongBao() { return maThongBao; }
    public void setMaThongBao(String maThongBao) { this.maThongBao = maThongBao; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getNguoiNhan() { return nguoiNhan; }
    public void setNguoiNhan(String nguoiNhan) { this.nguoiNhan = nguoiNhan; }

    public LocalDateTime getThoiGianTao() { return thoiGianTao; }
    public void setThoiGianTao(LocalDateTime thoiGianTao) { this.thoiGianTao = thoiGianTao; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}