package com.example.excelanalysis.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "calculated_data")
public class CalculatedData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "business_type")
    private String businessType;        // 业务类型
    
    @Column(name = "network_type")
    @Pattern(regexp = "^(4G|5G)$", message = "网络类型只能是4G或5G")
    private String networkType;         // 网络类型
    
    private LocalDate time;         // 时间（仅日期）
    
    @Column(name = "total_data_count")
    private Long totalDataCount;     // 总数据条数
    
    @Column(name = "received_count")
    private Long receivedCount;      // 接收条数
    
    @Column(name = "total_file_count")
    private Long totalFileCount;     // 总文件总数
    
    @Column(name = "received_file_count")
    private Long receivedFileCount;  // 接收文件总数
    
    @Column(name = "phone_null_rate")
    private Double phoneNullRate;       // 用户号码空值率
    
    @Column(name = "domain_null_rate")
    private Double domainNullRate;      // 域名空值率
    
    @Column(name = "dest_ip_null_rate")
    private Double destIpNullRate;      // 目的ip空值率
    
    @Column(name = "dest_port_null_rate")
    private Double destPortNullRate;    // 目的端口空值率
    
    @Column(name = "source_ip_null_rate")
    private Double sourceIpNullRate;    // 源公网ip空值率
    
    @Column(name = "source_port_null_rate")
    private Double sourcePortNullRate;  // 源端口空值率
    
    // 手动添加getter和setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBusinessType() {
        return businessType;
    }
    
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    
    public String getNetworkType() {
        return networkType;
    }
    
    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }
    
    public LocalDate getTime() {
        return time;
    }
    
    public void setTime(LocalDate time) {
        this.time = time;
    }
    
    public Long getTotalDataCount() {
        return totalDataCount;
    }
    
    public void setTotalDataCount(Long totalDataCount) {
        this.totalDataCount = totalDataCount;
    }
    
    public Long getReceivedCount() {
        return receivedCount;
    }
    
    public void setReceivedCount(Long receivedCount) {
        this.receivedCount = receivedCount;
    }
    
    public Long getTotalFileCount() {
        return totalFileCount;
    }
    
    public void setTotalFileCount(Long totalFileCount) {
        this.totalFileCount = totalFileCount;
    }
    
    public Long getReceivedFileCount() {
        return receivedFileCount;
    }
    
    public void setReceivedFileCount(Long receivedFileCount) {
        this.receivedFileCount = receivedFileCount;
    }
    
    public Double getPhoneNullRate() {
        return phoneNullRate;
    }
    
    public void setPhoneNullRate(Double phoneNullRate) {
        this.phoneNullRate = phoneNullRate;
    }
    
    public Double getDomainNullRate() {
        return domainNullRate;
    }
    
    public void setDomainNullRate(Double domainNullRate) {
        this.domainNullRate = domainNullRate;
    }
    
    public Double getDestIpNullRate() {
        return destIpNullRate;
    }
    
    public void setDestIpNullRate(Double destIpNullRate) {
        this.destIpNullRate = destIpNullRate;
    }
    
    public Double getDestPortNullRate() {
        return destPortNullRate;
    }
    
    public void setDestPortNullRate(Double destPortNullRate) {
        this.destPortNullRate = destPortNullRate;
    }
    
    public Double getSourceIpNullRate() {
        return sourceIpNullRate;
    }
    
    public void setSourceIpNullRate(Double sourceIpNullRate) {
        this.sourceIpNullRate = sourceIpNullRate;
    }
    
    public Double getSourcePortNullRate() {
        return sourcePortNullRate;
    }
    
    public void setSourcePortNullRate(Double sourcePortNullRate) {
        this.sourcePortNullRate = sourcePortNullRate;
    }
}