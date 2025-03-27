package com.example.excelanalysis.controller;

import com.example.excelanalysis.dto.DataStatisticsResponse;
import com.example.excelanalysis.service.DataComparisonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/data-comparison")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DataComparisonController {
    private static final Logger logger = LoggerFactory.getLogger(DataComparisonController.class);

    @Autowired
    private DataComparisonService dataComparisonService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        logger.info("收到文件上传请求: {}", file.getOriginalFilename());
        logger.info("Content-Type: {}", file.getContentType());
        logger.info("File size: {} bytes", file.getSize());
        
        try {
            if (file.isEmpty()) {
                logger.warn("上传的文件为空");
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "上传的文件为空");
                return ResponseEntity.badRequest().body(errorResult);
            }
            
            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
            logger.debug("文件类型: {}, 文件名: {}", contentType, fileName);
            
            boolean isValidExcel = contentType != null && 
                (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || 
                contentType.equals("application/vnd.ms-excel"));
            
            boolean isValidCsv = fileName != null && fileName.toLowerCase().endsWith(".csv") &&
                (contentType == null || contentType.equals("text/csv") || 
                contentType.equals("application/csv") || 
                contentType.equals("text/plain") ||
                contentType.equals("application/octet-stream"));
            
            if (!isValidExcel && !isValidCsv) {
                logger.warn("不支持的文件类型: {}", contentType);
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "只支持Excel文件(.xlsx或.xls)或CSV文件(.csv)");
                return ResponseEntity.badRequest().body(errorResult);
            }
            
            logger.info("开始处理上传的文件");
            Map<String, Object> result = dataComparisonService.processExcelUpload(file, "import");
            
            if (result.containsKey("success") && !(Boolean) result.get("success")) {
                logger.warn("处理失败: {}", result.get("message"));
                return ResponseEntity.badRequest().body(result);
            }
            
            logger.info("文件处理成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("文件上传处理异常", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "文件上传失败：" + e.getMessage());
            errorResult.put("error", e.getClass().getName());
            errorResult.put("stackTrace", e.getStackTrace()[0].toString());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        logger.info("收到模板下载请求");
        
        try {
            byte[] template = dataComparisonService.generateExcelTemplate();
            logger.info("成功生成Excel模板");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("数据导入模板.xlsx", java.nio.charset.StandardCharsets.UTF_8)
                    .build());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(template);
        } catch (Exception e) {
            logger.error("生成模板失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String networkType) {
        try {
            logger.info("接收到统计请求 - 日期: {}, 网络类型: {}", date, networkType);
            
            // 参数验证
            if (date == null || date.trim().isEmpty()) {
                logger.warn("日期参数为空");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "日期参数不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (networkType == null || networkType.trim().isEmpty()) {
                logger.warn("网络类型参数为空");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "网络类型参数不能为空");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            LocalDate queryDate;
            try {
                // 使用DateTimeFormatter确保日期格式正确
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                // 解析为LocalDate
                queryDate = LocalDate.parse(date, formatter);
                logger.info("成功解析日期: {}", queryDate);
            } catch (Exception e) {
                logger.error("日期格式错误: {}, 错误信息: {}", date, e.getMessage());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "日期格式错误，请使用 yyyy-MM-dd 格式");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // 调用服务层处理请求
            DataStatisticsResponse response = dataComparisonService.getDataStatistics(queryDate, networkType);
            
            // 获取响应数据
            Map<String, Object> responseMap = response.getResponseMap();
            if (responseMap == null) {
                responseMap = new HashMap<>();
                responseMap.put("data", new ArrayList<>());
                responseMap.put("total", 0);
            }
            
            logger.info("返回数据总数: {}", responseMap.get("total"));
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            logger.error("获取统计数据失败", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "获取统计数据失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }


    @GetMapping("/export")
    public ResponseEntity<Resource> exportExcel(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String networkType) {
        Resource file = dataComparisonService.exportExcel(date, networkType);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment")
                        .filename("数据比对结果.xlsx", java.nio.charset.StandardCharsets.UTF_8)
                        .build().toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}