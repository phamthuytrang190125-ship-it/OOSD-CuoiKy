package com.example.baocaosuco.repository;

import com.example.baocaosuco.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, String> {
    List<ThongBao> findByNguoiNhanOrderByThoiGianTaoDesc(String nguoiNhan);
}