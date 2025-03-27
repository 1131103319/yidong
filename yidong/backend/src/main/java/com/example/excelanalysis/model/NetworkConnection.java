package com.example.excelanalysis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "network_connection")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetworkConnection {
    
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
    
    @Column(name = "dest_ip")
    private String destIp;
    
    @Column(name = "dest_port")
    private Integer destPort;
    
    @Column(name = "source_ip")
    private String sourceIp;
    
    @Column(name = "source_port")
    private Integer sourcePort;
    
    @Column(name = "connection_time", nullable = false)
    private LocalDate connectionTime;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}