package com.example.excelanalysis.dto;

import java.util.List;
import java.util.Map;

public class DataStatisticsResponse {
    private String date;
    private String networkType;
    private List<String> businessTypes;
    private Map<String, Integer> totalDataCounts;
    private Map<String, Integer> receivedCounts;
    private Map<String, Integer> totalFileCounts;
    private Map<String, Integer> receivedFileCounts;
    private Map<String, Double> phoneNullRates;
    private Map<String, Double> domainNullRates;
    private Map<String, Double> destIpNullRates;
    private Map<String, Double> destPortNullRates;
    private Map<String, Double> sourceIpNullRates;
    private Map<String, Double> sourcePortNullRates;
    private Map<String, Object> responseMap;  // 新增字段，用于存储前端所需的格式化数据

    // Getters and setters
    public Map<String, Object> getResponseMap() {
        return responseMap;
    }

    public void setResponseMap(Map<String, Object> responseMap) {
        this.responseMap = responseMap;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public List<String> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(List<String> businessTypes) {
        this.businessTypes = businessTypes;
    }

    public Map<String, Integer> getTotalDataCounts() {
        return totalDataCounts;
    }

    public void setTotalDataCounts(Map<String, Integer> totalDataCounts) {
        this.totalDataCounts = totalDataCounts;
    }

    public Map<String, Integer> getReceivedCounts() {
        return receivedCounts;
    }

    public void setReceivedCounts(Map<String, Integer> receivedCounts) {
        this.receivedCounts = receivedCounts;
    }

    public Map<String, Integer> getTotalFileCounts() {
        return totalFileCounts;
    }

    public void setTotalFileCounts(Map<String, Integer> totalFileCounts) {
        this.totalFileCounts = totalFileCounts;
    }

    public Map<String, Integer> getReceivedFileCounts() {
        return receivedFileCounts;
    }

    public void setReceivedFileCounts(Map<String, Integer> receivedFileCounts) {
        this.receivedFileCounts = receivedFileCounts;
    }

    public Map<String, Double> getPhoneNullRates() {
        return phoneNullRates;
    }

    public void setPhoneNullRates(Map<String, Double> phoneNullRates) {
        this.phoneNullRates = phoneNullRates;
    }

    public Map<String, Double> getDomainNullRates() {
        return domainNullRates;
    }

    public void setDomainNullRates(Map<String, Double> domainNullRates) {
        this.domainNullRates = domainNullRates;
    }

    public Map<String, Double> getDestIpNullRates() {
        return destIpNullRates;
    }

    public void setDestIpNullRates(Map<String, Double> destIpNullRates) {
        this.destIpNullRates = destIpNullRates;
    }

    public Map<String, Double> getDestPortNullRates() {
        return destPortNullRates;
    }

    public void setDestPortNullRates(Map<String, Double> destPortNullRates) {
        this.destPortNullRates = destPortNullRates;
    }

    public Map<String, Double> getSourceIpNullRates() {
        return sourceIpNullRates;
    }

    public void setSourceIpNullRates(Map<String, Double> sourceIpNullRates) {
        this.sourceIpNullRates = sourceIpNullRates;
    }

    public Map<String, Double> getSourcePortNullRates() {
        return sourcePortNullRates;
    }

    public void setSourcePortNullRates(Map<String, Double> sourcePortNullRates) {
        this.sourcePortNullRates = sourcePortNullRates;
    }
}