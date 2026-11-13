package com.example.baocaosuco.repository; // Sửa lại tên package nếu cần

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.baocaosuco.entity.SuCo;

@Repository
public interface SuCoRepository extends JpaRepository<SuCo, String> {
    // Không cần viết gì thêm, Spring Data JPA đã lo hết!
    List<SuCo> findAllByOrderByNgayBaoCaoDesc();
}