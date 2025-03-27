package com.example.excelanalysis.repository;

import com.example.excelanalysis.model.SourceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface SourceDataRepository extends JpaRepository<SourceData, Long> {
    // 修改返回类型为List，因为可能有多条记录匹配同一业务类型和日期
    List<SourceData> findByBusinessTypeAndTime(String businessType, LocalDate time);
}