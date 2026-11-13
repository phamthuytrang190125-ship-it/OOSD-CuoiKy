package com.example.baocaosuco.repository;

import com.example.baocaosuco.entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HinhAnhRepository extends JpaRepository<HinhAnh, String> {
    List<HinhAnh> findByMaThamChieu(String maThamChieu);
}