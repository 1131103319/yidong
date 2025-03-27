package com.example.excelanalysis.dto;

import java.time.LocalDate;

public class ExcelDataDTO {
    private String businessType;
    private LocalDate time;
    private Long totalDataCount;
    private Long totalFileCount;

    // Getters and setters
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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

    public Long getTotalFileCount() {
        return totalFileCount;
    }

    public void setTotalFileCount(Long totalFileCount) {
        this.totalFileCount = totalFileCount;
    }
}