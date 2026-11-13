package com.example.baocaosuco.repository;

import com.example.baocaosuco.entity.KetQuaXuLy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaXuLyRepository extends JpaRepository<KetQuaXuLy, String> {
    KetQuaXuLy findByMaPhieu(String maPhieu);
}