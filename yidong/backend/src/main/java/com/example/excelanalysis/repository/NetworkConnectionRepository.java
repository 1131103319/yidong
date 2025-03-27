package com.example.excelanalysis.repository;

import com.example.excelanalysis.model.NetworkConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetworkConnectionRepository extends JpaRepository<NetworkConnection, Long> {
    List<NetworkConnection> findByDataUploadId(Long uploadId);
}