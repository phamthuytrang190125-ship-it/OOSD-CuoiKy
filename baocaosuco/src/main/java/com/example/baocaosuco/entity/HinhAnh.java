package com.example.baocaosuco.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HINH_ANH")
public class HinhAnh {
    @Id
    @Column(name = "MAHINHANH")
    private String maHinhAnh;

    @Column(name = "DUONGDANURL")
    private String duongDanURL;

    @Column(name = "KICHTHUOC")
    private Long kichThuoc;

    @Column(name = "LOAIHINHANH")
    private String loaiHinhAnh;

    @Column(name = "MATHAMCHIEU")
    private String maThamChieu;

    // Getters & Setters
    public String getMaHinhAnh() { return maHinhAnh; }
    public void setMaHinhAnh(String maHinhAnh) { this.maHinhAnh = maHinhAnh; }

    public String getDuongDanURL() { return duongDanURL; }
    public void setDuongDanURL(String duongDanURL) { this.duongDanURL = duongDanURL; }

    public Long getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(Long kichThuoc) { this.kichThuoc = kichThuoc; }

    public String getLoaiHinhAnh() { return loaiHinhAnh; }
    public void setLoaiHinhAnh(String loaiHinhAnh) { this.loaiHinhAnh = loaiHinhAnh; }

    public String getMaThamChieu() { return maThamChieu; }
    public void setMaThamChieu(String maThamChieu) { this.maThamChieu = maThamChieu; }
}