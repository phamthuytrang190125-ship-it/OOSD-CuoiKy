package com.example.baocaosuco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.baocaosuco.entity.PhieuPhanCong;

import java.util.List;

@Repository
public interface PhieuPhanCongRepository extends JpaRepository<PhieuPhanCong, String> {
    // Tìm các phiếu phân công theo mã tổ đội (TD01, TD02...)
    List<PhieuPhanCong> findByMaToDoiOrderByNgayLapDesc(String maToDoi);
}