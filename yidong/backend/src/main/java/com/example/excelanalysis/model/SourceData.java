package com.example.excelanalysis.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "source_data")
public class SourceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   @Column(name="ip")
    private String ip;
    @Column(name = "business_type")
    private String businessType;        // 业务类型
    
    @Column(name = "network_type")
    private String networkType;         // 网络类型
    
    @Column(name = "time")
    private LocalDate time;             // 时间
    
    @Column(name = "received_data_count")
    private Long receivedDataCount;  // 接收数据条数
    
    @Column(name = "received_file_count")
    private Long receivedFileCount;  // 接收文件总数
    
    @Column(name = "phone_null_count")
    private Long phoneNullCount;     // 号码空值数量
    
    @Column(name = "domain_null_count")
    private Long domainNullCount;    // 域名空值数量
    
    @Column(name = "dest_ip_null_count")
    private Long destIpNullCount;    // 目的ip空值数量
    
    @Column(name = "dest_port_null_count")
    private Long destPortNullCount;  // 目的端口空值数量
    
    @Column(name = "source_ip_null_count")
    private Long sourceIpNullCount;  // 源公网ip空值数量
    
    @Column(name = "source_port_null_count")
    private Long sourcePortNullCount; // 源端口空值数量
    
    // 手动添加getter和setter方法
    public Long getIp() {
        return receivedDataCount;
    }

    public void setIp(Long receivedDataCount) {
        this.receivedDataCount = receivedDataCount;
    }

    public Long getReceivedDataCount() {
        return receivedDataCount;
    }
    
    public void setReceivedDataCount(Long receivedDataCount) {
        this.receivedDataCount = receivedDataCount;
    }
    
    public Long getReceivedFileCount() {
        return receivedFileCount;
    }
    
    public void setReceivedFileCount(Long receivedFileCount) {
        this.receivedFileCount = receivedFileCount;
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
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getPhoneNullCount() {
        return phoneNullCount;
    }
    
    public void setPhoneNullCount(Long phoneNullCount) {
        this.phoneNullCount = phoneNullCount;
    }
    
    public Long getDomainNullCount() {
        return domainNullCount;
    }
    
    public void setDomainNullCount(Long domainNullCount) {
        this.domainNullCount = domainNullCount;
    }
    
    public Long getDestIpNullCount() {
        return destIpNullCount;
    }
    
    public void setDestIpNullCount(Long destIpNullCount) {
        this.destIpNullCount = destIpNullCount;
    }
    
    public Long getDestPortNullCount() {
        return destPortNullCount;
    }
    
    public void setDestPortNullCount(Long destPortNullCount) {
        this.destPortNullCount = destPortNullCount;
    }
    
    public Long getSourceIpNullCount() {
        return sourceIpNullCount;
    }
    
    public void setSourceIpNullCount(Long sourceIpNullCount) {
        this.sourceIpNullCount = sourceIpNullCount;
    }
    
    public Long getSourcePortNullCount() {
        return sourcePortNullCount;
    }
    
    public void setSourcePortNullCount(Long sourcePortNullCount) {
        this.sourcePortNullCount = sourcePortNullCount;
    }
}