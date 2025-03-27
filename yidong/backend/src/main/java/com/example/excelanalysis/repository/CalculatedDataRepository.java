package com.example.excelanalysis.repository;

import com.example.excelanalysis.model.CalculatedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalculatedDataRepository extends JpaRepository<CalculatedData, Long> {
    List<CalculatedData> findByBusinessType(String businessType);
    List<CalculatedData> findByTime(LocalDate date);
    List<CalculatedData> findByNetworkType(String networkType);
    List<CalculatedData> findByTimeAndNetworkType(LocalDate date, String networkType);
    
    @Override
    List<CalculatedData> findAll();
}