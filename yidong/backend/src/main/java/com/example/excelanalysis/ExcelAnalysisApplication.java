package com.example.excelanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExcelAnalysisApplication {
    // 保留原类名以避免大范围修改，但在注释中标明实际用途
    /**
     * 数据比对系统主应用类
     */
    public static void main(String[] args) {
        SpringApplication.run(ExcelAnalysisApplication.class, args);
    }
}