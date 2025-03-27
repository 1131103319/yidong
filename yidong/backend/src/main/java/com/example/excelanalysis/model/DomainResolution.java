package com.example.excelanalysis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "domain_resolution")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainResolution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "upload_id")
    private DataUpload dataUpload;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "domain")
    private String domain;
    
    @Column(name = "resolved_ip")
    private String resolvedIp;
    
    @Column(name = "resolution_time", nullable = false)
    private LocalDate resolutionTime;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}