package com.example.excelanalysis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_upload")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataUpload {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "business_type", nullable = false)
    private String businessType;
    
    @Column(name = "data_type", nullable = false)
    @Pattern(regexp = "^(4G|5G)$", message = "数据类型只能是4G或5G")
    private String dataType;
    
    @Column(name = "upload_time", nullable = false)
    private LocalDate uploadTime;
    
    @Column(name = "received_data_count", nullable = false)
    private Integer receivedDataCount;
    
    @Column(name = "received_file_count", nullable = false)
    private Integer receivedFileCount;
    
    @Column(name = "phone_null_count", nullable = false)
    private Integer phoneNullCount;
    
    @Column(name = "domain_null_count", nullable = false)
    private Integer domainNullCount;
    
    @Column(name = "dest_ip_null_count", nullable = false)
    private Integer destIpNullCount;
    
    @Column(name = "dest_port_null_count", nullable = false)
    private Integer destPortNullCount;
    
    @Column(name = "source_ip_null_count", nullable = false)
    private Integer sourceIpNullCount;
    
    @Column(name = "source_port_null_count", nullable = false)
    private Integer sourcePortNullCount;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}