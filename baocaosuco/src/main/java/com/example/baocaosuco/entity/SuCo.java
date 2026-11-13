package com.example.baocaosuco.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "SU_CO")
public class SuCo {

    @Id
    @Column(name = "MASUCO", length = 20)
    private String maSuCo;

    @Column(name = "NGAYBAOCAO")
    private LocalDateTime ngayBaoCao;

    @Column(name = "HOTENNGUOIBAO", length = 100)
    private String hoTenNguoiBao;

    @Column(name = "SODIENTHOAINGUOIBAO", length = 20)
    private String soDienThoaiNguoiBao;

    @Column(name = "DIACHI", length = 255)
    private String diaChi;

    @Column(name = "MALOAI", length = 20)
    private String maLoai;

    @Column(name = "MOTACHITIET", length = 500)
    private String moTaChiTiet;

    @Column(name = "TRANGTHAI", length = 50)
    private String trangThai;

    @Column(name = "LYDOTUCHOI", length = 255)
    private String lyDoTuChoi;

    @Column(name = "NGUOIDUYET", length = 20)
    private String nguoiDuyet;

    // Hàm phụ trợ để hiển thị tên loại sự cố dạng tiếng Việt trên giao diện Admin
    public String getTenLoai() {
        if ("LSC01".equals(maLoai)) return "Rác thải sinh hoạt";
        if ("LSC02".equals(maLoai)) return "Nước thải công nghiệp";
        if ("LSC03".equals(maLoai)) return "Ô nhiễm không khí";
        if ("LSC04".equals(maLoai)) return "Tiếng ồn";
        return maLoai != null ? maLoai : "Khác";
    }

    @PrePersist
    protected void onCreate() {
        if (this.ngayBaoCao == null) {
            this.ngayBaoCao = LocalDateTime.now();
        }
        if (this.trangThai == null || this.trangThai.isEmpty()) {
            this.trangThai = "Chờ duyệt";
        }
    }
}