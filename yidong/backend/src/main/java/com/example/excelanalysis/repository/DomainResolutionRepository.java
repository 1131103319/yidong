package com.example.excelanalysis.repository;

import com.example.excelanalysis.model.DomainResolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomainResolutionRepository extends JpaRepository<DomainResolution, Long> {
    List<DomainResolution> findByDataUploadId(Long uploadId);
}